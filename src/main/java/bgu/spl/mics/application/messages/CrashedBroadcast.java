package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

/**
 * CrashedBroadcast is a broadcast message indicating a system crash or failure.
 * It is used to notify all relevant microservices about a critical failure.
 */
public class CrashedBroadcast implements Broadcast {

    private final String reason;

    /**
     * Constructor for CrashedBroadcast.
     * @param reason A string describing the reason for the crash.
     */
    public CrashedBroadcast(String reason) {
        this.reason = reason;
    }

    /**
     * Gets the reason for the crash.
     * @return A string representing the reason.
     */
    public String getReason() {
        return reason;
    }
}
