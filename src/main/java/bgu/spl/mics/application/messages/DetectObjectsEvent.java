package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.DetectedObject;

import java.util.List;

/**
 * DetectObjectsEvent represents an event sent by a camera service when it detects objects.
 * The event contains a list of detected objects at a specific time.
 */
public class DetectObjectsEvent implements Event<List<DetectedObject>> {

    private final List<DetectedObject> detectedObjects;

    /**
     * Constructs a new DetectObjectsEvent.
     *
     * @param detectedObjects the list of objects detected by the camera.
     */
    public DetectObjectsEvent(List<DetectedObject> detectedObjects) {
        this.detectedObjects = detectedObjects;
    }

    /**
     * Returns the list of detected objects.
     *
     * @return the detected objects.
     */
    public List<DetectedObject> getDetectedObjects() {
        return detectedObjects;
    }
}