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

    public boolean updateMap(List<TrackedObject> trackedObjects, Pose currentPose) {
        boolean isNewItem = false;

        for (TrackedObject trackedObject : trackedObjects) {
            boolean isNew = true;

            // Iterate through existing landmarks
            for (LandMark landMark : landMarks) {
                if (landMark.getId().equals(trackedObject.getId())) {
                    // Update coordinates by averaging
                    List<CloudPoint> updatedCoordinates = new ArrayList<>();
                    List<CloudPoint> oldCoordinates = landMark.getCoordinates();
                    List<CloudPoint> newCoordinates = trackedObject.getCoordinates();

                    int size = Math.min(oldCoordinates.size(), newCoordinates.size());
                    for (int i = 0; i < size; i++) {
                        int avgX = (oldCoordinates.get(i).getX() + newCoordinates.get(i).getX()) / 2;
                        int avgY = (oldCoordinates.get(i).getY() + newCoordinates.get(i).getY()) / 2;
                        updatedCoordinates.add(new CloudPoint(avgX, avgY));
                    }

                    // Replace coordinates with the averaged values
                    landMark.setCoordinates(updatedCoordinates);
                    isNew = false;
                    break;
                }
            }

            // Add a new landmark if not found
            if (isNew) {
                landMarks.add(new LandMark(
                        trackedObject.getId(),
                        trackedObject.getDescription(),
                        trackedObject.getCoordinates()
                ));
                isNewItem = true;
            }
        }

        // Update pose history
        poses.add(currentPose);
        return isNewItem;
    }

}

