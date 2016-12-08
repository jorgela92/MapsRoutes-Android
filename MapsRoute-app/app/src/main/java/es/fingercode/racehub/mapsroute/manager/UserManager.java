package es.fingercode.racehub.mapsroute.manager;


import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import es.fingercode.racehub.mapsroute.api.Api;
import es.fingercode.racehub.mapsroute.model.Route;
import es.fingercode.racehub.mapsroute.model.User;

public class UserManager {

    private static UserManager instance;
    private static HashMap<String, String> parameters;
    private static Context context;

    private UserManager(Context context) {
        this.context = context;
    }


    public static UserManager getInstance(Context context) {
        //if no instance is initialized, create new instance
        //else return stored instance
        if (instance == null)
        {
            instance = new UserManager(context);
        }
        return instance;
    }

    private User userLogged;

    public User getUserLogged() {
        return userLogged;
    }

    public void login(String email, String pass, final Api.Listener<User> listener){
        Api.getInstance(context).postLogin(email, pass, new Api.Listener<User>() {
            @Override
            public void onSucces(User data) {
                userLogged = data;
                listener.onSucces(data);
            }

            @Override
            public void onError(String description) {
                listener.onError(description);
            }
        });
    }

    public User fromJson(JSONObject jsonObject){
        User user = new User();
        ArrayList<Route> routes = new ArrayList<Route>();
        try {
            if (jsonObject.has("id") ) {
                user.setId(jsonObject.getString("id"));
            }
            JSONArray routesJson = jsonObject.getJSONArray("routes");
            for ( int i = 0; i< routesJson.length(); i++ ) {
                JSONObject routeJson = routesJson.getJSONObject(i);
                routes.add(RouteManager.getInstance(context).fromJson(routeJson));
                user.setRoutes(routes);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}

