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

    public GPSIMU(int currentTick, Status status, List<Pose> poseList) {
        this.currentTick = currentTick;
        this.status = status;
        this.poseList = poseList;
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
}
