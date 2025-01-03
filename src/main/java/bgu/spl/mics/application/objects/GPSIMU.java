package bgu.spl.mics.application.objects;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Represents the robot's GPS and IMU system.
 * Provides information about the robot's position and movement.
 */
public class GPSIMU {
    private int currentTick;
    private Status status;
    private List<Pose> poseList;

    public GPSIMU(int currentTick, Status status, List<Pose> poseList) {
        this.currentTick = currentTick;
        this.status = status;
        this.poseList = poseList;
    }

    public GPSIMU() {
    }

    public enum Status { UP, DOWN, ERROR; }

    // Getters and Setters
    public int getCurrentTick() { return currentTick; }
    public void setCurrentTick(int currentTick) { this.currentTick = currentTick; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public List<Pose> getPoseList() { return poseList; }
    public void setPoseList(List<Pose> poseList) { this.poseList = poseList; }

    public Pose getPoseAtTime(int time) {
        for (Pose pose : poseList) {
            if (pose.getTime() == time) {
                return pose;
            }
        }
        return null;
    }

    /**
     * Loads pose data from a JSON file.
     *
     * @param path the path to the JSON file containing pose data.
     * @throws IOException if there is an error reading the file.
     */
    public void loadPoseData(String path) throws IOException {
        try (FileReader reader = new FileReader(path)) {
            Type poseListType = new TypeToken<List<Pose>>() {}.getType();
            this.poseList = new Gson().fromJson(reader, poseListType);
        }
    }
}
