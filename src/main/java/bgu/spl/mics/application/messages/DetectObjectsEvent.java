package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.DetectedObject;
import bgu.spl.mics.application.objects.StampedDetectedObjects;

import java.util.List;

/**
 * DetectObjectsEvent represents an event sent by a camera service when it detects objects.
 * The event contains a list of detected objects at a specific time.
 */
public class DetectObjectsEvent implements Event<Boolean> {
    private final StampedDetectedObjects detectedObjects;
    private final int timestamp;
    private final int cameraId;

    /**
     * Constructor for DetectObjectsEvent.
     * @param detectedObjects List of detected objects.
     * @param timestamp The time the objects were detected.
     */
    public DetectObjectsEvent(StampedDetectedObjects detectedObjects, int timestamp, int cameraId) {
        this.detectedObjects = detectedObjects;
        this.timestamp = timestamp;
        this.cameraId = cameraId;
    }

    /**
     * Gets the list of detected objects.
     * @return List of detected objects.
     */
    public StampedDetectedObjects getDetectedObjects() {
        return detectedObjects;
    }

    /**
     * Gets the timestamp when the objects were detected.
     * @return The timestamp.
     */
    public int getTimestamp() {
        return timestamp;
    }
    public int getCameraId() {
        return cameraId;
    }
}
