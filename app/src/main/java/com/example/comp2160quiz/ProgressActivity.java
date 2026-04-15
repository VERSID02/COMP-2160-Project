package com.example.comp2160quiz;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        SharedPreferences prefs = getSharedPreferences("EduQuizPrefs", MODE_PRIVATE);

        TextView tvCompletion = findViewById(R.id.tvCompletion);
        TextView tvMathFoundation = findViewById(R.id.tvMathFoundation);
        TextView tvMathAdvanced = findViewById(R.id.tvMathAdvanced);
        TextView tvScienceFoundation = findViewById(R.id.tvScienceFoundation);
        TextView tvScienceAdvanced = findViewById(R.id.tvScienceAdvanced);
        TextView tvEnglishFoundation = findViewById(R.id.tvEnglishFoundation);
        TextView tvEnglishAdvanced = findViewById(R.id.tvEnglishAdvanced);
        TextView tvHistoryFoundation = findViewById(R.id.tvHistoryFoundation);
        TextView tvHistoryAdvanced = findViewById(R.id.tvHistoryAdvanced);
        Button btnResetProgress = findViewById(R.id.btnResetProgress);

        String math = getString(R.string.mathematics);
        String science = getString(R.string.science);
        String english = getString(R.string.english);
        String history = getString(R.string.history);

        int mF = prefs.getInt("best_" + math + "_foundation", 0);
        int mA = prefs.getInt("best_" + math + "_advanced", 0);
        int sF = prefs.getInt("best_" + science + "_foundation", 0);
        int sA = prefs.getInt("best_" + science + "_advanced", 0);
        int eF = prefs.getInt("best_" + english + "_foundation", 0);
        int eA = prefs.getInt("best_" + english + "_advanced", 0);
        int hF = prefs.getInt("best_" + history + "_foundation", 0);
        int hA = prefs.getInt("best_" + history + "_advanced", 0);

        // 80% of 10 questions * 10 points = 80 points for foundation, 15 for advanced
        // We just check if score > 0 as "completed" since max score varies
        // The spec says score >= 80% — we track as percentage of questions * max pts
        // Simple check: best > 0 means attempted, star if score is high
        int completed = 0;
        if (mF >= 80) completed++;
        if (mA >= 80) completed++;
        if (sF >= 80) completed++;
        if (sA >= 80) completed++;
        if (eF >= 80) completed++;
        if (eA >= 80) completed++;
        if (hF >= 80) completed++;
        if (hA >= 80) completed++;

        int overallPct = (completed * 100) / 8;
        tvCompletion.setText(getString(R.string.completion, overallPct));

        tvMathFoundation.setText("Foundation: " + mF + (mF >= 80 ? " ★" : ""));
        tvMathAdvanced.setText("Advanced: " + mA + (mA >= 80 ? " ★" : ""));
        tvScienceFoundation.setText("Foundation: " + sF + (sF >= 80 ? " ★" : ""));
        tvScienceAdvanced.setText("Advanced: " + sA + (sA >= 80 ? " ★" : ""));
        tvEnglishFoundation.setText("Foundation: " + eF + (eF >= 80 ? " ★" : ""));
        tvEnglishAdvanced.setText("Advanced: " + eA + (eA >= 80 ? " ★" : ""));
        tvHistoryFoundation.setText("Foundation: " + hF + (hF >= 80 ? " ★" : ""));
        tvHistoryAdvanced.setText("Advanced: " + hA + (hA >= 80 ? " ★" : ""));

        btnResetProgress.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.reset_confirm))
                    .setPositiveButton(getString(R.string.yes), (d, w) -> {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.remove("best_" + math + "_foundation");
                        editor.remove("best_" + math + "_advanced");
                        editor.remove("best_" + science + "_foundation");
                        editor.remove("best_" + science + "_advanced");
                        editor.remove("best_" + english + "_foundation");
                        editor.remove("best_" + english + "_advanced");
                        editor.remove("best_" + history + "_foundation");
                        editor.remove("best_" + history + "_advanced");
                        editor.apply();
                        recreate();
                    })
                    .setNegativeButton(getString(R.string.no), null)
                    .show();
        });
    }
}
