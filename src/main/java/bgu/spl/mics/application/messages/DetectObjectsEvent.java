package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.DetectedObject;

import java.util.List;

/**
 * DetectObjectsEvent represents an event sent by a camera service when it detects objects.
 * The event contains a list of detected objects at a specific time.
 */
class DetectObjectsEvent implements Event<Boolean> {

    private final List<DetectedObject> detectedObjects;
    private final int timestamp;

    /**
     * Constructor for DetectObjectsEvent.
     * @param detectedObjects List of detected objects.
     * @param timestamp The time the objects were detected.
     */
    public DetectObjectsEvent(List<DetectedObject> detectedObjects, int timestamp) {
        this.detectedObjects = detectedObjects;
        this.timestamp = timestamp;
    }

    /**
     * Gets the list of detected objects.
     * @return List of detected objects.
     */
    public List<DetectedObject> getDetectedObjects() {
        return detectedObjects;
    }

    /**
     * Gets the timestamp when the objects were detected.
     * @return The timestamp.
     */
    public int getTimestamp() {
        return timestamp;
    }
}