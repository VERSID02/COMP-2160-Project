package com.example.comp2160quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PlayerSetupActivity extends AppCompatActivity {

    EditText etName;
    RadioGroup rgGrade;
    RadioButton rbJunior, rbSenior;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_setup);

        etName = findViewById(R.id.etName);
        rgGrade = findViewById(R.id.rgGrade);
        rbJunior = findViewById(R.id.rbJunior);
        rbSenior = findViewById(R.id.rbSenior);
        btnSave = findViewById(R.id.btnSave);


        SharedPreferences prefs = getSharedPreferences("EduQuizPrefs", MODE_PRIVATE);
        String savedName = prefs.getString("player_name", "");
        String savedGrade = prefs.getString("player_grade", "");
        etName.setText(savedName);
        if (savedGrade.equals("Senior")) {
            rbSenior.setChecked(true);
        } else {
            rbJunior.setChecked(true);
        }

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            }
            int selectedId = rgGrade.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Please select a grade", Toast.LENGTH_SHORT).show();
                return;
            }
            String grade = (selectedId == R.id.rbJunior) ? "Junior" : "Senior";

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("player_name", name);
            editor.putString("player_grade", grade);
            editor.apply();

            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}
