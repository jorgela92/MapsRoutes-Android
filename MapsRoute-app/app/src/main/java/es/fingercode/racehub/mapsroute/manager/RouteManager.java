package es.fingercode.racehub.mapsroute.manager;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import es.fingercode.racehub.mapsroute.api.Api;
import es.fingercode.racehub.mapsroute.model.Route;
import es.fingercode.racehub.mapsroute.model.WayPoint;

/**
 * Created by jorge on 6/12/16.
 */

public class RouteManager {
    private static RouteManager instance;
    private static HashMap<String, String> parameters;
    private static Context context;

    private RouteManager(Context context) {
        this.context = context;
    }

    public static RouteManager getInstance(Context context) {
        //if no instance is initialized, create new instance
        //else return stored instance
        if (instance == null)
        {
            instance = new RouteManager(context);
        }
        return instance;
    }
    public void addRoute(String id, String title, String descriptio, final Api.Listener<Route> listener){
        Api.getInstance(context).postAddRoute(id, title, descriptio, new Api.Listener<Route>() {
            @Override
            public void onSucces(Route data) {
                listener.onSucces(data);
            }

            @Override
            public void onError(String description) {
                listener.onError(description);
            }
        });

    }
    public Route fromJson(JSONObject jsonObject){
        Route route = new Route();
        ArrayList<WayPoint> waypoints = new ArrayList<WayPoint>();
        try {
            if (jsonObject.has("id") ) {
                route.setId(jsonObject.getString("id"));
            }
            if(jsonObject.has("title")){
                route.setTitle(jsonObject.getString("title"));
            }
            if(jsonObject.has("description")){
                route.setDescription(jsonObject.getString("description"));
            }
            JSONArray wayPointsJson = jsonObject.getJSONArray("waypoints");
            for (int i = 0; i< wayPointsJson.length(); i++ ) {
                JSONObject waiPointJson = wayPointsJson.getJSONObject(i);
                waypoints.add(WayPointManager.getInstance(context).fromJson(waiPointJson));
                route.setWaypoints(waypoints);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return route;
    }
}