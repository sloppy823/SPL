package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.PoseEvent;
import bgu.spl.mics.application.messages.TerminatedBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrackedObjectsEvent;
import bgu.spl.mics.application.objects.FusionSlam;
import bgu.spl.mics.application.objects.Pose;
import bgu.spl.mics.application.objects.TrackedObject;

import java.util.ArrayList;
import java.util.List;

/**
 * FusionSlamService integrates data from multiple sensors to build and update
 * the robot's global map.
 *
 * This service receives TrackedObjectsEvents from LiDAR workers and PoseEvents from the PoseService,
 * transforming and updating the map with new landmarks.
 */
public class FusionSlamService extends MicroService {

    private final FusionSlam fusionSlam;
    private Pose currentPose;

    /**
     * Constructor for FusionSlamService.
     *
     * @param fusionSlam The FusionSLAM object responsible for managing the global map.
     */
    public FusionSlamService(FusionSlam fusionSlam) {
        super("FusionSlamService");
        this.fusionSlam = fusionSlam;
        this.currentPose = null;
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
            currentPose = event.getPose();
            System.out.println(getName() + " updated pose to: " + currentPose);
        });

        // Subscribe to TrackedObjectsEvent to process tracked objects
        subscribeEvent(TrackedObjectsEvent.class, event -> {
            
            List<TrackedObject> trackedObjects = event.getTrackedObjects();
            if (currentPose != null && trackedObjects != null) {
                boolean isNewItem = fusionSlam.updateMap(trackedObjects, currentPose);
                System.out.println(getName() + " updated map with tracked objects. New items added: " + isNewItem);
            }
        });

        // Subscribe to TerminatedBroadcast for shutdown signals
        subscribeBroadcast(TerminatedBroadcast.class, termination -> {
            System.out.println(getName() + " received TerminationBroadcast. Terminating.");
            terminate();
        });
    }
}