package com.example.comp2160quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("EduQuizPrefs", MODE_PRIVATE);
            String name = prefs.getString("player_name", null);

            if (name != null && !name.isEmpty()) {
                startActivity(new Intent(this, MainActivity.class));
            } else {
                startActivity(new Intent(this, PlayerSetupActivity.class));
            }
            finish();
        }, 2000);
    }
}
