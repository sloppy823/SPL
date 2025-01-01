package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.Camera;
import bgu.spl.mics.application.objects.StampedDetectedObjects;

import java.util.List;

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
        super("CameraService-" + camera.getId());
        this.camera = camera;
    }

    /**
     * Initializes the CameraService.
     * Registers the service to handle TickBroadcasts and sets up callbacks for sending
     * DetectObjectsEvents.
     */
    @Override
    protected void initialize() {

        // Subscribe to TickBroadcast
        subscribeBroadcast(TickBroadcast.class, tickBroadcast -> {
            currentTick = tickBroadcast.getTick();
            int realtime = currentTick- camera.getFrequency();

            // Check if the camera should send events based on its frequency
            if (realtime > 0) {
                StampedDetectedObjects stampedObjects = camera.getDetectedObjectsAtTime(realtime);
                if (stampedObjects != null) {
                    // Create and send a DetectObjectsEvent
                    DetectObjectsEvent event = new DetectObjectsEvent(stampedObjects, stampedObjects.getTime(),camera.getId());
                    // Send the event and handle the result asynchronously
                    sendEvent(event).resolve(true);
                }
        }});

            // Subscribe to TerminatedBroadcast
        subscribeBroadcast(TerminatedBroadcast.class, termination -> {
            System.out.println(getName() + " received TerminationBroadcast. Terminating.");
            terminate();
        });
        

    }
}
