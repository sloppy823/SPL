package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The main entry point for the GurionRock Pro Max Ultra Over 9000 simulation.
 * <p>
 * This class initializes the system and starts the simulation by setting up
 * services, objects, and configurations.
 * </p>
 */
public class GurionRockRunner {

    /**
     * The main method of the simulation.
     * This method sets up the necessary components, parses configuration files,
     * initializes services, and starts the simulation.
     *
     * @param args Command-line arguments. The first argument is expected to be the path to the configuration file.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Missing configuration file argument.");
            return;
        }

        // Parse configuration file
        String configFilePath = args[0];
        try (FileReader reader = new FileReader(configFilePath)) {

            // Read configuration
            JsonObject config = JsonParser.parseReader(reader).getAsJsonObject();

            // Parse Cameras
            List<Camera> cameras = new ArrayList<>();
            Type cameraListType = new TypeToken<List<Camera>>() {}.getType();
            cameras = new Gson().fromJson(config.get("Cameras"), cameraListType);

            // Parse LiDAR Workers
            List<LiDarWorkerTracker> lidarWorkers = new ArrayList<>();
            Type lidarListType = new TypeToken<List<LiDarWorkerTracker>>() {}.getType();
            lidarWorkers = new Gson().fromJson(config.get("LiDarWorkers"), lidarListType);

            // Parse GPSIMU (Pose Data)
            String posePath = config.get("PoseDataPath").getAsString();
            GPSIMU gpsimu = new GPSIMU();
            gpsimu.loadPoseData(posePath);

            // Initialize FusionSLAM
            FusionSlam fusionSlam = new FusionSlam();

            // Parse Timing Parameters
            int tickTime = config.get("TickTime").getAsInt();
            int duration = config.get("Duration").getAsInt();

            // Create Thread Pool for Services
            ExecutorService executor = Executors.newCachedThreadPool();

            // Initialize and Start Services
            for (Camera camera : cameras) {
                executor.execute(new CameraService(camera));
            }
            for (LiDarWorkerTracker lidar : lidarWorkers) {
                LiDarDataBase db = LiDarDataBase.getInstance(config.get("LiDarDataPath").getAsString());
                executor.execute(new LiDarService(lidar, db));
            }
            executor.execute(new PoseService(gpsimu));
            executor.execute(new FusionSlamService(fusionSlam));
            executor.execute(new TimeService(tickTime, duration));

            // Wait for all services to complete
            executor.shutdown();

            // Write Output File
            writeOutputFile(configFilePath, fusionSlam, gpsimu);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeOutputFile(String configFilePath, FusionSlam fusionSlam, GPSIMU gpsimu) {
        try {
            String outputPath = configFilePath.replace("config.json", "output_file.json");
            JsonObject output = new JsonObject();

            // Add statistics
            output.addProperty("systemRuntime", gpsimu.getCurrentTick());
            output.addProperty("numDetectedObjects", StatisticalFolder.getNumDetectedObjects());
            output.addProperty("numTrackedObjects", StatisticalFolder.getNumTrackedObjects());
            output.addProperty("numLandmarks", fusionSlam.getLandMarks().size());

            // Add landmarks
            output.add("landMarks", new Gson().toJsonTree(fusionSlam.getLandMarks()));

            // Write to file
            try (FileWriter writer = new FileWriter(outputPath)) {
                new Gson().toJson(output, writer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
