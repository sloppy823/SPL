package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.DetectObjectsEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.Camera;
import bgu.spl.mics.application.objects.StampedDetectedObjects;

/**
 * CameraService is responsible for processing data from the camera and
 * sending DetectObjectsEvents to LiDAR workers.
 * 
 * This service interacts with the Camera object to detect objects and updates
 * the system's StatisticalFolder upon sending its observations.
 */
public class CameraService extends MicroService {
    Camera camera;
    private int currentTick = 0;


    /**
     * Constructor for CameraService.
     *
     * @param camera The Camera object that this service will use to detect objects.
     */
    public CameraService(Camera camera) {
        super("cameraService");
        this.camera = camera;

        // TODO Implement this);
    }

    /**
     * Initializes the CameraService.
     * Registers the service to handle TickBroadcasts and sets up callbacks for sending
     * DetectObjectsEvents.
     */
    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, tickBroadcast -> {
            currentTick = tickBroadcast.getTick();

            // Check if it's time to act based on frequency
            if (currentTick % camera.getFrequency() == 0) {
                for (StampedDetectedObjects stampedObject : camera.getDetectedObjectsList()) {
                    if (stampedObject.getTime() == currentTick) {
                        // Create and send a DetectObjectsEvent
                        DetectObjectsEvent event = new DetectObjectsEvent(stampedObject.getDetectedObjects());
                        sendEvent(event);
            }
        }
    }
});
    }
}
