package bgu.spl.mics.application.objects;

import java.util.List;

/**
 * Represents a camera sensor on the robot.
 * Responsible for detecting objects in the environment.
 */
public class Camera {
    private int id;
    private int frequency;
    private Status status;
    private List<StampedDetectedObjects> detectedObjectsList;

    public enum Status { UP, DOWN, ERROR; }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getFrequency() { return frequency; }
    public void setFrequency(int frequency) { this.frequency = frequency; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public List<StampedDetectedObjects> getDetectedObjectsList() { return detectedObjectsList; }
    public void setDetectedObjectsList(List<StampedDetectedObjects> detectedObjectsList) { this.detectedObjectsList = detectedObjectsList; }
}
