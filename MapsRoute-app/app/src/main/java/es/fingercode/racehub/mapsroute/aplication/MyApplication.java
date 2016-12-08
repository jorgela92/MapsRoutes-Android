package es.fingercode.racehub.mapsroute.aplication;

import android.app.Application;


/**
 * Created by fc2 on 25/10/16.
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //addFabric();
    }
    public void addFabric(){
        //Fabric.with(this, new Crashlytics());
    }

}
