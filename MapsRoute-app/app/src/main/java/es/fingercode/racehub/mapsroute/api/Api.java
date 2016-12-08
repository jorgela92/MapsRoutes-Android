package es.fingercode.racehub.mapsroute.api;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.fingercode.racehub.mapsroute.manager.RouteManager;
import es.fingercode.racehub.mapsroute.manager.UserManager;
import es.fingercode.racehub.mapsroute.manager.WayPointManager;
import es.fingercode.racehub.mapsroute.model.Route;
import es.fingercode.racehub.mapsroute.model.User;
import es.fingercode.racehub.mapsroute.model.WayPoint;

/**
 * Created by fc2 on 4/10/16.
 */

public class Api {
    private static final String URL="http://192.168.1.130";
    private static final String PORT=":8000";
    private static final String POST_LOGIN="/api/v1/login/";
    private static final String GET_ROUTES="/api/v1/routes/";
    private static final String POST_ROUTE="/api/v1/addroute/";
    private static final String POST_WAYPOINT="/api/v1/addwaypoint/";
    private static RequestQueue requestQueue;
    private static Context context;

    public interface Listener<T> {
        void onSucces(T data);
        void onError(String description);
    }

    private static Api instance;
    private Api(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public static Api getInstance(Context contextA) {
        //if no instance is initialized, create new instance
        //else return stored instance
        if (instance == null)
        {
            context = contextA;
            instance = new Api(contextA);
        }
        return instance;
    }
    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void postLogin(String email, String password, final Listener<User> listener){
        RequestQueue queue = getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, URL+PORT+POST_LOGIN+email+"/"+password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(response);
                    listener.onSucces(UserManager.getInstance(context).fromJson(jsonResponse));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    listener.onError(e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {;
                listener.onError(error.getMessage());
            }
        });
        queue.add(request);
    }
    public void postAddRoute(String id, final String title, final String description, final Listener<Route> listener){
        RequestQueue queue = getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, URL+PORT+POST_ROUTE+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(response);
                    listener.onSucces(RouteManager.getInstance(context).fromJson(jsonResponse));
                }
                catch (JSONException e) {
                    e.printStackTrace();

                    listener.onError(e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {;
                listener.onError(handleError(error));
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("desciption", description);
                return params;

            }
        };

        queue.add(request);
    }
    public void postAddWaypiont(String id, final String title, final String description, final String latitude, final String longitude, final Listener<WayPoint> listener){
        RequestQueue queue = getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, URL+PORT+POST_WAYPOINT+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(response);
                    listener.onSucces(WayPointManager.getInstance(context).fromJson(jsonResponse));
                }
                catch (JSONException e) {
                    e.printStackTrace();

                    listener.onError(e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(handleError(error));
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("description", description);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                return params;

            }
        };

        queue.add(request);
    }

    String handleError(VolleyError error) {
        if ( error.networkResponse != null ) {
            return new String(error.networkResponse.data);
        }
        else {
            return error.getMessage();
        }
    }

}
