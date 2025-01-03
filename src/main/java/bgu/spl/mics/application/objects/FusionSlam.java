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
    private List<Pose> poses;//maybe not needed

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
    public Pose getPoseAtTime(int timestamp){
        for (Pose pose : poses) {
            if (pose.getTime() == timestamp) {
                return pose;
            }
        }
        return null;
        //if (poses.size()>=timestamp) return poses.get(timestamp-1);
    }

    public List<Pose> getPoses() {
        return poses;
    }

    private CloudPoint convertToGlobalCoordinates(CloudPoint point, Pose botPose) {
        // Convert yaw angle from degrees to radians
        double yawRad = Math.toRadians(botPose.getYaw());

        // Calculate the global coordinates
        double globalX = (Math.cos(yawRad) * point.getX()) - (Math.sin(yawRad) * point.getY()) + botPose.getX();
        double globalY = (Math.sin(yawRad) * point.getX()) + (Math.cos(yawRad) * point.getY()) + botPose.getY();

        return new CloudPoint(globalX, globalY);
    }

    private CloudPoint averagePoint(CloudPoint oldPoint, CloudPoint newPoint){
        double avgX = (oldPoint.getX() + newPoint.getX()) / 2;
        double avgY = (oldPoint.getY() + newPoint.getY()) / 2;
        return new CloudPoint(avgX, avgY);
    }

    private List<CloudPoint> convertToGlobalCoordinates(List<CloudPoint> points, Pose botPose) {
        List<CloudPoint> globalPoints = new ArrayList<>();
        for (CloudPoint point : points) {
            globalPoints.add(convertToGlobalCoordinates(point, botPose));
        }
        return globalPoints;
    }
    private List<CloudPoint> averageCoordinates(List<CloudPoint> oldCoordinates, List<CloudPoint> newCoordinates, Pose currentPose){
        List<CloudPoint> updatedCoordinates = new ArrayList<>();
        int size = Math.min(oldCoordinates.size(), newCoordinates.size());
        for (int i = 0; i < size; i++) {
            CloudPoint oldPoint = oldCoordinates.get(i);//already in global
            CloudPoint newPoint = convertToGlobalCoordinates(newCoordinates.get(i), currentPose);
            updatedCoordinates.add(averagePoint(oldPoint, newPoint));
        }
        if (oldCoordinates.size()>size) {
            for (int i = size; i < oldCoordinates.size(); i++) {
                updatedCoordinates.add(oldCoordinates.get(i));
            }
        }
        if (newCoordinates.size()>size) {
            for (int i = size; i < newCoordinates.size(); i++) {
                updatedCoordinates.add(convertToGlobalCoordinates(newCoordinates.get(i), currentPose));
            }
        }
        return updatedCoordinates;
    }



    public boolean updateMap(List<TrackedObject> trackedObjects, Pose currentPose) {
        boolean isThereANewItem = false;

        for (TrackedObject trackedObject : trackedObjects) {
            boolean isNew = true;

            // Iterate through existing landmarks
            for (LandMark landMark : landMarks) {
                if (landMark.getId().equals(trackedObject.getId())) {
                    // Update coordinates by averaging
                    List<CloudPoint> oldCoordinates = landMark.getCoordinates();
                    List<CloudPoint> newCoordinates = trackedObject.getCoordinates();
                    List<CloudPoint> updatedCoordinates = averageCoordinates(oldCoordinates, newCoordinates, currentPose);
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
                        convertToGlobalCoordinates(trackedObject.getCoordinates(), currentPose)
                ));
                isThereANewItem = true;
            }
        }

        // Update pose history
        poses.add(currentPose);
        return isThereANewItem;
    }

}

