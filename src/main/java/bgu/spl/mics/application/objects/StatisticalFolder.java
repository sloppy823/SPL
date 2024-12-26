package bgu.spl.mics.application.objects;

/**
 * Holds statistical information about the system's operation.
 * This class aggregates metrics such as the runtime of the system,
 * the number of objects detected and tracked, and the number of landmarks identified.
 */
public class StatisticalFolder {
    private int systemRuntime;
    private int numDetectedObjects;
    private int numTrackedObjects;
    private int numLandmarks;

    // Getters and Setters
    public int getSystemRuntime() { return systemRuntime; }
    public void setSystemRuntime(int systemRuntime) { this.systemRuntime = systemRuntime; }

    public int getNumDetectedObjects() { return numDetectedObjects; }
    public void setNumDetectedObjects(int numDetectedObjects) { this.numDetectedObjects = numDetectedObjects; }

    public int getNumTrackedObjects() { return numTrackedObjects; }
    public void setNumTrackedObjects(int numTrackedObjects) { this.numTrackedObjects = numTrackedObjects; }

    public int getNumLandmarks() { return numLandmarks; }
    public void setNumLandmarks(int numLandmarks) { this.numLandmarks = numLandmarks; }
}