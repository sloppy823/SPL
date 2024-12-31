package bgu.spl.mics.application.objects;

import java.util.ArrayList;
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
    private int currentTick;

    public LiDarWorkerTracker(int id, int frequency, Status status, List<TrackedObject> lastTrackedObjects, int currentTick) {
        this.id = id;
        this.frequency = frequency;
        this.status = status;
        this.lastTrackedObjects = lastTrackedObjects;
        this.currentTick = currentTick;
    }

  public void processDetectedObjects(List<DetectedObject> objects, int time) {
      LiDarDataBase dataBase = LiDarDataBase.getInstance(null);
      for (DetectedObject object : objects) {
          StampedCloudPoints point = null;
          for (StampedCloudPoints p : dataBase.getCloudPoints()) {
              if (p.getId().equals(object.getId())) {
                  point = p;
                  break;
              }
          }
          if (point != null)
              lastTrackedObjects.add(new TrackedObject(object.getId(), time, object.getDescription(), point.getCloudPoints()));
      }
  }
    public enum Status { UP, DOWN, ERROR; }

    // Getters and Setters
    public int getCurrentTick() { return currentTick; }
    public void setCurrentTick(int currentTick) { this.currentTick = currentTick; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getFrequency() { return frequency; }
    public void setFrequency(int frequency) { this.frequency = frequency; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public List<TrackedObject> getLastTrackedObjects() { return lastTrackedObjects; }
    public void setLastTrackedObjects(List<TrackedObject> lastTrackedObjects) { this.lastTrackedObjects = lastTrackedObjects; }
}




