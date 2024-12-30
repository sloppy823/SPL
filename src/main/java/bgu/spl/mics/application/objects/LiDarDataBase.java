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
        loadData(filePath);
        return SingletonHolder.instance;
    }

    private void loadData(String filePath) {
//        Gson gson = new Gson();
//        File file = new File(filePath);
//        String fileName = file.getName();
//        try (FileReader reader = new FileReader(fileName)) {
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}


    public List<StampedCloudPoints> getCloudPoints() {
        return cloudPoints;
    }

    public void addCloudPoint(StampedCloudPoints point) {
        cloudPoints.add(point);
    }

}
