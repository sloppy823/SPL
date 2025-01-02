package bgu.spl.mics.application.objects;

import java.util.List;

/**
 * Represents objects detected by the camera at a specific timestamp.
 * Includes the time of detection and a list of detected objects.
 */
public class StampedDetectedObjects {
    private int time;
    private List<DetectedObject> detectedObjects;

    public StampedDetectedObjects() {
        this.time = 0;
        this.detectedObjects = null;
    }

    public StampedDetectedObjects(int time, List<DetectedObject> detectedObjects) {
        this.time = time;
        this.detectedObjects = detectedObjects;
    }

    // Getters and Setters
    public int getTime() { return time; }
    public void setTime(int time) { this.time = time; }

    public List<DetectedObject> getDetectedObjects() { return detectedObjects; }
    public void setDetectedObjects(List<DetectedObject> detectedObjects) { this.detectedObjects = detectedObjects; }
}
