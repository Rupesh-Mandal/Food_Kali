package com.softkali.sellerkf.splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.softkali.sellerkf.BuildConfig;
import com.softkali.sellerkf.R;
import com.softkali.sellerkf.auth.LoginActivity;
import com.softkali.sellerkf.dashboard.DashboardActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    TextView v;
    private static int SPLASH_SCREEN = 2000;

    SharedPreferences sharedPreferences;
    JSONObject userObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        v = findViewById(R.id.textView2);
        v.setText("V "+ BuildConfig.VERSION_NAME);;
        sharedPreferences = getSharedPreferences("Food_Kali", Context.MODE_PRIVATE);
        try {
            String user=sharedPreferences.getString("user","");
            userObject =new JSONObject(user);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               if (userObject==null){
                   startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                   finish();
               }else {
                   startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                   finish();
               }
            }
        },SPLASH_SCREEN);
    }

}