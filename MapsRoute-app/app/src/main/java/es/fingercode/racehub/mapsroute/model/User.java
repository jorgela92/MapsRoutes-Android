package es.fingercode.racehub.mapsroute.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User {
    private String id, date_creation,username, password, salt, name,email;
    private ArrayList<Route> routes;

    public User() {
        this.routes = new ArrayList<Route>();
    }

    public String getId() {
        return id;
    }

    public String getDate_creation() {
        return date_creation;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate_creation(String date_creation) {
        this.date_creation = date_creation;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}