package es.fingercode.racehub.mapsroute.manager;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import es.fingercode.racehub.mapsroute.api.Api;
import es.fingercode.racehub.mapsroute.model.WayPoint;

/**
 * Created by jorge on 6/12/16.
 */

public class WayPointManager {
    private static WayPointManager instance;
    private static HashMap<String, String> parameters;
    private static Context context;

    private WayPointManager(Context context) {
        this.context = context;
    }

    public static WayPointManager getInstance(Context context) {
        //if no instance is initialized, create new instance
        //else return stored instance
        if (instance == null)
        {
            instance = new WayPointManager(context);
        }
        return instance;
    }
    public void addWaypoint(String id, String title, String descriptio,String latitude, String longitude, final Api.Listener<WayPoint> listener){
        Api.getInstance(context).postAddWaypiont(id, title, descriptio,latitude,longitude, new Api.Listener<WayPoint>() {
            @Override
            public void onSucces(WayPoint data) {
                listener.onSucces(data);
            }

            @Override
            public void onError(String description) {
                listener.onError(description);
            }
        });
    }
    public WayPoint fromJson(JSONObject jsonObject){
        WayPoint wayPoint = new WayPoint();
        try {
            if (jsonObject.has("id") ) {
                wayPoint.setId(jsonObject.getString("id"));
            }
            if(jsonObject.has("title")){
                wayPoint.setTitle(jsonObject.getString("title"));
            }
            if(jsonObject.has("description")){
                wayPoint.setDescription(jsonObject.getString("description"));
            }
            if(jsonObject.has("latitude")){
                wayPoint.setLatitude(jsonObject.getDouble("latitude"));
            }
            if(jsonObject.has("longitude")){
                wayPoint.setLongitude(jsonObject.getDouble("longitude"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return wayPoint;
    }
}
