package com.example.set;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import com.example.set.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "my_app_preferences";
    public static final String APP_PREFERENCES_TOKEN = "auth_token";

    SharedPreferences appPreferences;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        token = appPreferences.getString(APP_PREFERENCES_TOKEN, "");
        if (token.length() > 0) {
            ((Game) findViewById(R.id.game)).init(token);
        } else {
            openLoginScreen();
        }
    }
    
    public void openLoginScreen() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String token = data.getStringExtra("token");
                SharedPreferences.Editor editor =  appPreferences.edit();
                editor.putString(APP_PREFERENCES_TOKEN, token);
                editor.apply();
                ((Game) findViewById(R.id.game)).init(token);
            }
        }
    }

    public void showConnectionError() {
        showToast(R.string.connection_error);
    }

    public void showToast(int message) {
        Toast.makeText(getApplicationContext(),
                message, Toast.LENGTH_LONG).show();
    }
}
