package com.example.comp2160quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences prefs = getSharedPreferences("EduQuizPrefs", MODE_PRIVATE);

        EditText etNameEdit = findViewById(R.id.etNameEdit);
        RadioGroup rgGradeEdit = findViewById(R.id.rgGradeEdit);
        RadioButton rbJuniorEdit = findViewById(R.id.rbJuniorEdit);
        RadioButton rbSeniorEdit = findViewById(R.id.rbSeniorEdit);
        Switch switchSound = findViewById(R.id.switchSound);
        Switch switchExplanations = findViewById(R.id.switchExplanations);
        RadioGroup rgQuestionCount = findViewById(R.id.rgQuestionCount);
        RadioButton rb5 = findViewById(R.id.rb5);
        RadioButton rb10 = findViewById(R.id.rb10);
        RadioButton rb15 = findViewById(R.id.rb15);
        Button btnSaveSettings = findViewById(R.id.btnSaveSettings);
        Button btnResetAll = findViewById(R.id.btnResetAll);

        // Load current values
        etNameEdit.setText(prefs.getString("player_name", ""));
        String grade = prefs.getString("player_grade", "Junior");
        if (grade.equals("Senior")) rbSeniorEdit.setChecked(true);
        else rbJuniorEdit.setChecked(true);

        switchSound.setChecked(prefs.getBoolean("sound_enabled", true));
        switchExplanations.setChecked(prefs.getBoolean("show_explanations", true));

        int qCount = prefs.getInt("question_count", 10);
        if (qCount == 5) rb5.setChecked(true);
        else if (qCount == 15) rb15.setChecked(true);
        else rb10.setChecked(true);

        btnSaveSettings.setOnClickListener(v -> {
            String newName = etNameEdit.getText().toString().trim();
            if (newName.isEmpty()) {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            int selectedGradeId = rgGradeEdit.getCheckedRadioButtonId();
            String newGrade = (selectedGradeId == R.id.rbJuniorEdit) ? "Junior" : "Senior";

            int selectedCountId = rgQuestionCount.getCheckedRadioButtonId();
            int newCount = 10;
            if (selectedCountId == R.id.rb5) newCount = 5;
            else if (selectedCountId == R.id.rb15) newCount = 15;

            prefs.edit()
                    .putString("player_name", newName)
                    .putString("player_grade", newGrade)
                    .putBoolean("sound_enabled", switchSound.isChecked())
                    .putBoolean("show_explanations", switchExplanations.isChecked())
                    .putInt("question_count", newCount)
                    .apply();

            // Toggle music based on sound setting
            if (switchSound.isChecked()) {
                startService(new Intent(this, BackgroundMusicService.class));
            } else {
                stopService(new Intent(this, BackgroundMusicService.class));
            }

            Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnResetAll.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.reset_confirm))
                    .setPositiveButton(getString(R.string.yes), (d, w) -> {
                        prefs.edit().clear().apply();
                        startActivity(new Intent(this, PlayerSetupActivity.class));
                        finish();
                    })
                    .setNegativeButton(getString(R.string.no), null)
                    .show();
        });
    }
}
