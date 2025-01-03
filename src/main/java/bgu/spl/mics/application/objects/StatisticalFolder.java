package bgu.spl.mics.application.objects;

/**
 * Holds statistical information about the system's operation.
 * This class aggregates metrics such as the runtime of the system,
 * the number of objects detected and tracked, and the number of landmarks identified.
 */
public class StatisticalFolder {
    private int systemRuntime;
    private static int numDetectedObjects;
    private static int numTrackedObjects;
    private int numLandmarks;

    public StatisticalFolder(int systemRuntime, int numDetectedObjects, int numTrackedObjects, int numLandmarks) {
        this.systemRuntime = systemRuntime;
        this.numDetectedObjects = numDetectedObjects;
        this.numTrackedObjects = numTrackedObjects;
        this.numLandmarks = numLandmarks;
    }

    // Getters and Setters
    public int getSystemRuntime() { return systemRuntime; }
    public void setSystemRuntime(int systemRuntime) { this.systemRuntime = systemRuntime; }

    public static int getNumDetectedObjects() { return numDetectedObjects; }
    public void setNumDetectedObjects(int numDetectedObjects) { this.numDetectedObjects = numDetectedObjects; }

    public static int getNumTrackedObjects() { return numTrackedObjects; }
    public void setNumTrackedObjects(int numTrackedObjects) { this.numTrackedObjects = numTrackedObjects; }

    public int getNumLandmarks() { return numLandmarks; }
    public void setNumLandmarks(int numLandmarks) { this.numLandmarks = numLandmarks; }
}