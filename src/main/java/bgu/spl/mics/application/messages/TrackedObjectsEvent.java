package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.TrackedObject;

import java.util.List;

class TrackedObjectsEvent implements Event<Void> {

    private final List<TrackedObject> trackedObjects;
    private final int timestamp;

    /**
     * Constructor for TrackedObjectsEvent.
     * @param trackedObjects List of tracked objects.
     * @param timestamp The time the objects were tracked.
     */
    public TrackedObjectsEvent(List<TrackedObject> trackedObjects, int timestamp) {
        this.trackedObjects = trackedObjects;
        this.timestamp = timestamp;
    }

    /**
     * Gets the list of tracked objects.
     * @return List of tracked objects.
     */
    public List<TrackedObject> getTrackedObjects() {
        return trackedObjects;
    }

    /**
     * Gets the timestamp when the objects were tracked.
     * @return The timestamp.
     */
    public int getTimestamp() {
        return timestamp;
    }
}
