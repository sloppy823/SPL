package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CrashedBroadcast;
import bgu.spl.mics.application.messages.PoseEvent;
import bgu.spl.mics.application.messages.TerminatedBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.GPSIMU;
import bgu.spl.mics.application.objects.Pose;

import java.util.ArrayList;
import java.util.List;

/**
 * PoseService is responsible for maintaining the robot's current pose (position and orientation)
 * and broadcasting PoseEvents at every tick.
 */
public class PoseService extends MicroService {

    private final GPSIMU gpsimu;
    private List<Pose> poses;

    /**
     * Constructor for PoseService.
     *
     * @param gpsimu The GPSIMU object that provides the robot's pose data.
     */
    public PoseService(GPSIMU gpsimu) {
        super("PoseService");
        this.gpsimu = gpsimu;
        poses = new ArrayList<>();
    }

    /**
     * Initializes the PoseService.
     * Subscribes to TickBroadcast and sends PoseEvents at every tick based on the current pose.
     */
    @Override
    protected void initialize() {
        // Subscribe to TickBroadcast
        subscribeBroadcast(TickBroadcast.class, tickBroadcast -> {
            // Update the current tick from the broadcast
            int currentTick = tickBroadcast.getTick();
            gpsimu.setCurrentTick(currentTick);

            // Get the current pose and send a PoseEvent
            Pose currentPose = gpsimu.getPoseAtTime(currentTick);
            if (currentPose != null)
                sendEvent(new PoseEvent(currentPose, currentTick));
            else {
                System.out.println(getName() + " could not retrieve pose at tick " + currentTick);
                sendBroadcast(new TerminatedBroadcast(getName()));
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