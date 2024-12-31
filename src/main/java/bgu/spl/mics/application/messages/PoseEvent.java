package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.Pose;

/**
 * PoseEvent represents an event that provides the robot's current pose.
 * Sent by: PoseService
 * Handled by: Fusion-SLAM
 */
public class PoseEvent implements Event<Pose> {

    private final Pose pose;
    private final int timestamp;

    /**
     * Constructor for PoseEvent.
     * @param pose The robot's current pose.
     * @param timestamp The time at which the pose is recorded.
     */
    public PoseEvent(Pose pose, int timestamp) {
        this.pose = pose;
        this.timestamp = timestamp;
    }

    /**
     * Gets the pose associated with this event.
     * @return The robot's pose.
     */
    public Pose getPose() {
        return pose;
    }

    /**
     * Gets the timestamp when the pose was recorded.
     * @return The timestamp of the pose.
     */
    public int getTimestamp() {
        return timestamp;
    }
}