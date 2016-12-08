package es.fingercode.racehub.mapsroute.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import es.fingercode.racehub.mapsroute.R;

/**
 * Created by jorge on 3/10/16.
 */

public class SplashActivity extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME = 3000;
    public static final String PREFS_NAME = "AOP_PREFS";
    public static final String PREFS_USER = "AOP_PREFS_USER_String";
    public static final String PREFS_PASS = "AOP_PREFS_PASS_String";
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                boolean check = false;
            if(getPrefsUser(context) == null && getPrefsPass(context) == null){
                // Start activity
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                i.putExtra("check_tracking", check);
                startActivity(i);
                // Close activity
                finish();
            }else {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                i.putExtra("check_tracking", check);
                startActivity(i);
                finish();
            }
            }
        }, SPLASH_TIME);
    }

    public String getPrefsUser(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(PREFS_USER, null);
        return text;
    }
    public String getPrefsPass(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(PREFS_PASS, null);
        return text;
    }

}