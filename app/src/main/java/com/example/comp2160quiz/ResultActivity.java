package com.example.comp2160quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.stream.IntStream;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        String subject = getIntent().getStringExtra("subject");
        String tier = getIntent().getStringExtra("tier");
        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 10);
        int[] playerAnswers = getIntent().getIntArrayExtra("player_answers");
        int[] correctAnswers = getIntent().getIntArrayExtra("correct_answers");

        TextView tvResultTitle = findViewById(R.id.tvResultTitle);
        TextView tvResultDetails = findViewById(R.id.tvResultDetails);
        TextView tvGradeLabel = findViewById(R.id.tvGradeLabel);
        TextView tvPerformanceMsg = findViewById(R.id.tvPerformanceMsg);
        TextView tvNewBest = findViewById(R.id.tvNewBest);
        RecyclerView rvReview = findViewById(R.id.rvReview);
        Button btnTryAgain = findViewById(R.id.btnTryAgain);
        Button btnChangeSubject = findViewById(R.id.btnChangeSubject);
        Button btnShare = findViewById(R.id.btnShare);
        Button btnGoHome = findViewById(R.id.btnGoHome);

        tvResultTitle.setText(subject + " - " + tier);

        int numCorrect = (int) IntStream.range(0, correctAnswers.length).filter(i -> playerAnswers[i] == correctAnswers[i]).count();
        int numWrong = total - numCorrect;
        int percentage = (total == 0) ? 0 : (numCorrect * 100 / total);

        tvResultDetails.setText("Score: " + score + "  Correct: " + numCorrect + "  Wrong: " + numWrong + "  " + percentage + "%");

        String gradeLabel;
        if (percentage >= 90) gradeLabel = "A";
        else if (percentage >= 75) gradeLabel = "B";
        else if (percentage >= 60) gradeLabel = "C";
        else if (percentage >= 50) gradeLabel = "D";
        else gradeLabel = "F";
        tvGradeLabel.setText(gradeLabel);

        String perfMsg;
        if (percentage >= 80) perfMsg = "Excellent! You have mastered this topic.";
        else if (percentage >= 60) perfMsg = "Good effort! Keep revising to improve further.";
        else perfMsg = "Keep practising — review the topics you missed.";
        tvPerformanceMsg.setText(perfMsg);

        SharedPreferences prefs = getSharedPreferences("EduQuizPrefs", MODE_PRIVATE);
        String key = "best_" + subject + "_" + tier.toLowerCase();
        int currentBest = prefs.getInt(key, 0);
        if (score > currentBest) {
            prefs.edit().putInt(key, score).apply();
            tvNewBest.setVisibility(android.view.View.VISIBLE);
            tvNewBest.setText(getString(R.string.new_best));
        }

        List<Question> questions = QuestionBank.getQuestions(this, subject, tier);

        for (int i = 0; i < questions.size() && i < playerAnswers.length; i++) {
            questions.get(i).setPlayerAnswerIndex(playerAnswers[i]);
        }
        ReviewAdapter adapter = new ReviewAdapter(this, questions);
        rvReview.setLayoutManager(new LinearLayoutManager(this));
        rvReview.setAdapter(adapter);

        btnTryAgain.setOnClickListener(v -> {
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra("subject", subject);
            intent.putExtra("tier", tier);
            startActivity(intent);
            finish();
        });

        btnChangeSubject.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        btnGoHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        btnShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    "I scored " + numCorrect + "/" + total + " on " + tier + " " + subject + " in EduQuiz!");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });
    }
}
