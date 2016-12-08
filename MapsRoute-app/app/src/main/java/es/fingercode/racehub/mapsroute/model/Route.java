package es.fingercode.racehub.mapsroute.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jorge on 28/11/16.
 */

public class Route {
    private String id,title,description,date_creation;
    private ArrayList<WayPoint> waypoints;

    public Route() {
        this.waypoints = new ArrayList<WayPoint>();
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(String date_creation) {
        this.date_creation = date_creation;
    }

    public ArrayList<WayPoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(ArrayList<WayPoint> waypoints) {
        this.waypoints = waypoints;
    }


}
