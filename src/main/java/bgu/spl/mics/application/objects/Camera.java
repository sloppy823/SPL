package bgu.spl.mics.application.objects;

import java.util.List;

/**
 * Represents a camera sensor on the robot.
 * Responsible for detecting objects in the environment.
 */
public class Camera {
    private int id;
    private int frequency;
    private STATUS status;
    private List<StampedDetectedObjects> detectedObjectsList;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getFrequency() { return frequency; }
    public void setFrequency(int frequency) { this.frequency = frequency; }

    public STATUS getStatus() { return status; }
    public void setStatus(STATUS status) { this.status = status; }

    public List<StampedDetectedObjects> getDetectedObjectsList() { return detectedObjectsList; }
    public void setDetectedObjectsList(List<StampedDetectedObjects> detectedObjectsList) { this.detectedObjectsList = detectedObjectsList; }
}
