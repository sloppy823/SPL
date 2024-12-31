package bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the fusion of sensor data for simultaneous localization and mapping (SLAM).
 * Combines data from multiple sensors (e.g., LiDAR, camera) to build and update a global map.
 * Implements the Singleton pattern to ensure a single instance of FusionSlam exists.
 */
public class FusionSlam {

    private List<LandMark> landMarks;
    private List<Pose> poses;

    // Singleton instance holder
    private static class FusionSlamHolder {
        private static final FusionSlam instance = new FusionSlam();
    }

    private FusionSlam() {
        landMarks = new ArrayList<>();
        poses = new ArrayList<>();
    }

    public static FusionSlam getInstance() {
        return FusionSlamHolder.instance;
    }

    public void addLandMark(LandMark landMark) {
        landMarks.add(landMark);
    }

    public List<LandMark> getLandMarks() {
        return landMarks;
    }

    public void addPose(Pose pose) {
        poses.add(pose);
    }

    public List<Pose> getPoses() {
        return poses;
    }

    public void updateMap(List<TrackedObject> trackedObjects, Pose currentPose) {
        for (TrackedObject trackedObject : trackedObjects) {
            boolean isNew = true;
            for (LandMark landMark : landMarks) {
                if (landMark.getId().equals(trackedObject.getId())) {
                    isNew = false;
                    break;
                }
            }
            if (isNew) {
                landMarks.add(new LandMark(trackedObject.getId(), trackedObject.getDescription(), trackedObject.getCoordinates()));
            }
        }
        poses.add(currentPose);
    }
}

