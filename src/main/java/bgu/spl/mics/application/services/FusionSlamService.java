package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.FusionSlam;
import bgu.spl.mics.application.objects.Pose;
import bgu.spl.mics.application.objects.TrackedObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FusionSlamService integrates data from multiple sensors to build and update
 * the robot's global map.
 *
 * This service receives TrackedObjectsEvents from LiDAR workers and PoseEvents from the PoseService,
 * transforming and updating the map with new landmarks.
 */
public class FusionSlamService extends MicroService {

    private final FusionSlam fusionSlam;
    private final ConcurrentHashMap<Integer, List<TrackedObjectsEvent>> pendingTrackedObjectsEvents; // Pending tracked objects events

    /**
     * Constructor for FusionSlamService.
     *
     * @param fusionSlam The FusionSLAM object responsible for managing the global map.
     */
    public FusionSlamService(FusionSlam fusionSlam) {
        super("FusionSlamService");
        this.fusionSlam = fusionSlam;
        this.pendingTrackedObjectsEvents = new ConcurrentHashMap<>();
    }

    /**
     * Initializes the FusionSlamService.
     * Registers the service to handle TrackedObjectsEvents, PoseEvents, and TickBroadcasts,
     * and sets up callbacks for updating the global map.
     */
    @Override
    protected void initialize() {
        // Subscribe to PoseEvent to update the current pose
        subscribeEvent(PoseEvent.class, event -> {
            int timestamp = event.getTimestamp();
            Pose pose = event.getPose();
            fusionSlam.addPose(pose);
            System.out.println(" added pose: " + pose + " at timestamp: " + timestamp);
            if (pendingTrackedObjectsEvents.containsKey(timestamp)) {
                List<TrackedObjectsEvent> trackedObjectsEvents = pendingTrackedObjectsEvents.remove(timestamp);
                for (TrackedObjectsEvent trackedObjectsEvent : trackedObjectsEvents) {
                    fusionSlam.updateMap(trackedObjectsEvent.getTrackedObjects(), pose);
                    System.out.println(" updated map with tracked objects: " + trackedObjectsEvent.getTrackedObjects() + " at timestamp: " + timestamp);
                }
            }
        });

        // Subscribe to TrackedObjectsEvent to process tracked objects
        subscribeEvent(TrackedObjectsEvent.class, event -> {
            int eventTimestamp = event.getTimestamp();
            List<TrackedObject> trackedObjects = event.getTrackedObjects();
            Pose position = fusionSlam.getPoseAtTime(eventTimestamp);
            if(position != null){
                fusionSlam.updateMap(trackedObjects, position);
                System.out.println(" updated map with tracked objects: " + trackedObjects + " at timestamp: " + eventTimestamp);
            }
            else {
                System.out.println(getName() + " waiting for pose at timestamp " + eventTimestamp);
                pendingTrackedObjectsEvents.computeIfAbsent(eventTimestamp, k -> new ArrayList<>()).add(event);
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
