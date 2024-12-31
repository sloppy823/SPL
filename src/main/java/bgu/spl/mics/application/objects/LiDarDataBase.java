package bgu.spl.mics.application.objects;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;
//import java.util.List;
import java.io.File;

/**
 * LiDarDataBase is a singleton class responsible for managing LiDAR data.
 * It provides access to cloud point data and other relevant information for tracked objects.
 */
public class LiDarDataBase {

    private final List<StampedCloudPoints> cloudPoints;
    private String filePath;

    private static class SingletonHolder {
        private static LiDarDataBase instance = new LiDarDataBase();
    }

    private LiDarDataBase() {
        cloudPoints = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of LiDarDataBase.
     *
     * @param filePath The path to the LiDAR data file.
     * @return The singleton instance of LiDarDataBase.
     */

    public static LiDarDataBase getInstance(String filePath) {
        LiDarDataBase instance = SingletonHolder.instance;
        instance.filePath = filePath;
        instance.loadData();
        return instance;
    }

    private void loadData() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filePath)) {
            Type listType = new TypeToken<List<StampedCloudPoints>>() {}.getType();
            List<StampedCloudPoints> data = gson.fromJson(reader, listType);
            if (data != null) {
                cloudPoints.addAll(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<StampedCloudPoints> getCloudPoints() {
        return cloudPoints;
    }

    public void addCloudPoint(StampedCloudPoints point) {
        cloudPoints.add(point);
    }
}
