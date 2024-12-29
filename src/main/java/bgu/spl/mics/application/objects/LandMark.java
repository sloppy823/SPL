package bgu.spl.mics.application.objects;

import java.util.List;

/**
 * Represents a landmark in the environment map.
 * Landmarks are identified and updated by the FusionSlam service.
 */
public class LandMark {
    private int id;
    private String description;
    private List<CloudPoint> coordinates;

    public LandMark(int id, String description, List<CloudPoint> coordinates) {
        this.id = id;
        this.description = description;
        this.coordinates = coordinates;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<CloudPoint> getCoordinates() { return coordinates; }
    public void setCoordinates(List<CloudPoint> coordinates) { this.coordinates = coordinates; }
}
