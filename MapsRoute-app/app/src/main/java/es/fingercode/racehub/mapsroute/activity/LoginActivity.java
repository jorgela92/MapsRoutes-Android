package es.fingercode.racehub.mapsroute.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import es.fingercode.racehub.mapsroute.R;
import es.fingercode.racehub.mapsroute.api.Api;
import es.fingercode.racehub.mapsroute.manager.UserManager;
import es.fingercode.racehub.mapsroute.model.User;

/**
 * Created by jorge on 3/10/16.
 */

public class LoginActivity extends AppCompatActivity implements OnClickListener, Api.Listener<User> {
    private EditText editTextUser, editTextPass;
    private TextInputLayout tilName, tilPass;
    private String nameUserET, passUserET;
    private Button buttonEnter;
    public static final String PREFS_NAME = "AOP_PREFS";
    public static final String PREFS_USER = "AOP_PREFS_USER_String";
    public static final String PREFS_PASS = "AOP_PREFS_PASS_String";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createUi();
    }

    public void createUi() {
        tilName = (TextInputLayout) findViewById(R.id.til_user);
        tilPass = (TextInputLayout) findViewById(R.id.til_pass);
        editTextUser = (EditText) findViewById(R.id.edittext_user);
        editTextPass = (EditText) findViewById(R.id.edittext_pass);
        buttonEnter = (Button) findViewById(R.id.button_enter);
        buttonEnter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        nameUserET = editTextUser.getText().toString();
        passUserET = editTextPass.getText().toString();
        onLogin(v);
    }

    private boolean validateEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean validatePass(String password) {
       if (password!=null){
           return true;
       }else {
           return false;
       }
    }

    public void onLogin(View v) {
        switch (v.getId()) {
            case R.id.button_enter:
                if (validateEmail(nameUserET) == true && validatePass(passUserET) == true) {
                    //save(this, nameUserET, passUserET);
                    UserManager.getInstance(this).login(nameUserET, passUserET, this);
                } else {
                    tilName.setError("Introduzca su email");
                    tilPass.setError("Introduzca contraseña correcta");
                }
                break;
        }
    }

    public void save(Context context, String textUser, String textPass) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(PREFS_USER, textUser);
        editor.putString(PREFS_PASS, textPass);
        editor.commit();
    }

    @Override
    public void onSucces(User data) {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", data.getId());
        i.putExtra("user", bundle);
        startActivity(i);
        finish();
    }

    @Override
    public void onError(String description) {
        Log.i("ee", description);
    }
}

