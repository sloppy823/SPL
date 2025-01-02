package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminatedBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;

/**
 * TimeService acts as the global timer for the system, broadcasting TickBroadcast messages
 * at regular intervals and controlling the simulation's duration.
 */
public class TimeService extends MicroService {

    private final int tickTime;
    private final int duration;
    private int tickCount;

    /**
     * Constructor for TimeService.
     *
     * @param TickTime  The duration of each tick in milliseconds.
     * @param Duration  The total number of ticks before the service terminates.
     */
    public TimeService(int TickTime, int Duration) {
        super("TimeService");
        this.tickTime = TickTime *1000;
        this.duration = Duration;
        this.tickCount = 0;

    }

    /**
     * Initializes the TimeService.
     * Starts broadcasting TickBroadcast messages and terminates after the specified duration.
     */
    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, br->{
            if (duration < tickCount) {
                sendBroadcast(new TerminatedBroadcast(getName()));
            }else {
                try {
                    long longTime = (long) (tickTime);
                    Thread.sleep(longTime);
                    tickCount++;
                    sendBroadcast(new TickBroadcast(tickCount));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        subscribeBroadcast(TerminatedBroadcast.class, br->{terminate();});

        sendBroadcast(new TickBroadcast(tickCount));
    }
}
