package com.example.comp2160quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("EduQuizPrefs", MODE_PRIVATE);
        int launchCount = prefs.getInt("launch_count", 0);
        prefs.edit().putInt("launch_count", launchCount + 1).apply();

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        TextView tvGrade = findViewById(R.id.tvGrade);
        RecyclerView rvSubjects = findViewById(R.id.rvSubjects);
        Button btnProgress = findViewById(R.id.btnProgress);
        Button btnSettings = findViewById(R.id.btnSettings);

        String name = prefs.getString("player_name", "Player");
        String grade = prefs.getString("player_grade", "Junior");

        tvWelcome.setText(getString(R.string.welcome_back, name));
        tvGrade.setText(getString(R.string.grade_label, grade));

        List<String> subjects = Arrays.asList(
                getString(R.string.mathematics),
                getString(R.string.science),
                getString(R.string.english),
                getString(R.string.history)
        );
        List<String> colors = Arrays.asList("#2196F3", "#4CAF50", "#FF9800", "#F44336");

        SubjectAdapter adapter = new SubjectAdapter(this, subjects, colors, subject -> {
            Intent intent = new Intent(this, SubjectActivity.class);
            intent.putExtra("subject", subject);
            startActivity(intent);
        });

        rvSubjects.setLayoutManager(new LinearLayoutManager(this));
        rvSubjects.setAdapter(adapter);

        btnProgress.setOnClickListener(v -> startActivity(new Intent(this, ProgressActivity.class)));
        btnSettings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        // Start background music
        startService(new Intent(this, BackgroundMusicService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView rvSubjects = findViewById(R.id.rvSubjects);
        if (rvSubjects.getAdapter() != null) {
            rvSubjects.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, BackgroundMusicService.class));
    }
}
