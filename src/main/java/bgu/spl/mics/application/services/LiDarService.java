package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.DetectedObject;
import bgu.spl.mics.application.objects.LiDarWorkerTracker;
import bgu.spl.mics.application.objects.TrackedObject;
import java.util.List;

/**
 * LiDarService is responsible for processing data from the LiDAR sensor and
 * sending TrackedObjectsEvents to the FusionSLAM service.
 *
 * This service interacts with the LiDarWorkerTracker object to retrieve and process
 * cloud point data and updates the system's StatisticalFolder upon sending its
 * observations.
 */
public class LiDarService extends MicroService {

    private final LiDarWorkerTracker liDarTracker;
    private int currentTick = 0;

    /**
     * Constructor for LiDarService.
     *
     * @param liDarTracker The LiDAR tracker object that this service will use to process data.
     */
    public LiDarService(LiDarWorkerTracker liDarTracker) {
        super("LiDarService-" + liDarTracker.getId());
        this.liDarTracker = liDarTracker;
    }

    /**
     * Initializes the LiDarService.
     * Registers the service to handle DetectObjectsEvents and TickBroadcasts,
     * and sets up the necessary callbacks for processing data.
     */
    @Override
    protected void initialize() {

        // Subscribe to TickBroadcast for synchronization
        subscribeBroadcast(TickBroadcast.class, tickBroadcast -> {
            currentTick = tickBroadcast.getTick();
            liDarTracker.setCurrentTick(currentTick);
        });

        // Subscribe to DetectObjectsEvent
        subscribeEvent(DetectObjectsEvent.class, event -> {
            // Process only if the LiDAR is active and at the correct frequency
            int eventTime = event.getTimestamp();
            if (liDarTracker.getStatus() == LiDarWorkerTracker.Status.UP && currentTick >= eventTime + liDarTracker.getFrequency()) {

                // Process detected objects into tracked objects
                liDarTracker.processDetectedObjects(event.getDetectedObjects().getDetectedObjects(), currentTick);

                // Send TrackedObjectsEvent to Fusion-SLAM
                List<TrackedObject> trackedObjects = liDarTracker.getLastTrackedObjects();
                if (trackedObjects != null && !trackedObjects.isEmpty()) {
                    Future<Void> result = sendEvent(new TrackedObjectsEvent(trackedObjects, currentTick));
                    if (result != null) {
                        result.resolve(null);
                    }
                }
            }
        });

        // Subscribe to TerminatedBroadcast for shutdown signals
        subscribeBroadcast(TerminatedBroadcast.class, termination -> {
            System.out.println(getName() + " received TerminationBroadcast. Terminating.");
            terminate();
        });

        subscribeBroadcast(CrashedBroadcast.class, termination -> {
            System.out.println(getName() + " received CrashedBroadcast. Terminating.");
            terminate();
        });
    }
}
