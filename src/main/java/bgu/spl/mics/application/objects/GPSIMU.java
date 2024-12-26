package bgu.spl.mics.application.objects;

import java.util.List;

/**
 * Represents the robot's GPS and IMU system.
 * Provides information about the robot's position and movement.
 */
public class GPSIMU {
    private int currentTick;
    private Status status;
    private List<Pose> poseList;

    public enum Status { UP, DOWN, ERROR; }

    // Getters and Setters
    public int getCurrentTick() { return currentTick; }
    public void setCurrentTick(int currentTick) { this.currentTick = currentTick; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public List<Pose> getPoseList() { return poseList; }
    public void setPoseList(List<Pose> poseList) { this.poseList = poseList; }
}
