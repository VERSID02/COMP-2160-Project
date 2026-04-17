package com.example.comp2160quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SubjectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        String subject = getIntent().getStringExtra("subject");

        TextView tvSubjectTitle = findViewById(R.id.tvSubjectTitle);
        TextView tvSubjectDesc = findViewById(R.id.tvSubjectDesc);
        TextView tvBestFoundation = findViewById(R.id.tvBestFoundation);
        TextView tvBestAdvanced = findViewById(R.id.tvBestAdvanced);
        Button btnFoundation = findViewById(R.id.btnFoundation);
        Button btnAdvanced = findViewById(R.id.btnAdvanced);

        tvSubjectTitle.setText(subject);

        String desc = "";
        if (subject.equals(getString(R.string.mathematics))) desc = getString(R.string.math_desc);
        else if (subject.equals(getString(R.string.science))) desc = getString(R.string.science_desc);
        else if (subject.equals(getString(R.string.french))) desc = getString(R.string.french_desc);
        else if (subject.equals(getString(R.string.history))) desc = getString(R.string.history_desc);
        tvSubjectDesc.setText(desc);

        SharedPreferences prefs = getSharedPreferences("EduQuizPrefs", MODE_PRIVATE);
        int bestF = prefs.getInt("best_" + subject + "_foundation", 0);
        int bestA = prefs.getInt("best_" + subject + "_advanced", 0);
        tvBestFoundation.setText(getString(R.string.best_score, bestF));
        tvBestAdvanced.setText(getString(R.string.best_score, bestA));

        btnFoundation.setOnClickListener(v -> {
            prefs.edit().putString("last_subject", subject).apply();
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra("subject", subject);
            intent.putExtra("tier", getString(R.string.foundation));
            startActivity(intent);
        });

        btnAdvanced.setOnClickListener(v -> {
            prefs.edit().putString("last_subject", subject).apply();
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra("subject", subject);
            intent.putExtra("tier", getString(R.string.advanced));
            startActivity(intent);
        });
    }
}
