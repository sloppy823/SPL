package bgu.spl.mics.application.objects;

import java.util.List;

/**
 * Represents a group of cloud points corresponding to a specific timestamp.
 * Used by the LiDAR system to store and process point cloud data for tracked objects.
 */
public class StampedCloudPoints {
    private String id;
    private int time;
    private List<CloudPoint> cloudPoints;

    public StampedCloudPoints(String id, int time, List<CloudPoint> cloudPoints) {
        this.id = id;
        this.time = time;
        this.cloudPoints = cloudPoints;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getTime() { return time; }
    public void setTime(int time) { this.time = time; }

    public List<CloudPoint>  getCloudPoints() { return cloudPoints; }
    public void setCloudPoints(List<CloudPoint>  cloudPoints) { this.cloudPoints = cloudPoints; }
}
