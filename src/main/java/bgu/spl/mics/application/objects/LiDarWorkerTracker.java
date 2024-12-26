package bgu.spl.mics.application.objects;

import java.util.List;

/**
 * LiDarWorkerTracker is responsible for managing a LiDAR worker.
 * It processes DetectObjectsEvents and generates TrackedObjectsEvents by using data from the LiDarDataBase.
 * Each worker tracks objects and sends observations to the FusionSlam service.
 */
public class LiDarWorkerTracker {
    private int id;
    private int frequency;
    private Status status;
    private List<TrackedObject> lastTrackedObjects;

    public enum Status { UP, DOWN, ERROR; }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getFrequency() { return frequency; }
    public void setFrequency(int frequency) { this.frequency = frequency; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public List<TrackedObject> getLastTrackedObjects() { return lastTrackedObjects; }
    public void setLastTrackedObjects(List<TrackedObject> lastTrackedObjects) { this.lastTrackedObjects = lastTrackedObjects; }
}
