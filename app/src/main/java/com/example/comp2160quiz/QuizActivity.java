package com.example.comp2160quiz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class QuizActivity extends AppCompatActivity {

    TextView tvSubjectTier, tvQuestionNum, tvScore, tvTimer, tvQuestion, tvExplanation;
    Button btnA, btnB, btnC, btnD;
    ProgressBar progressBar;

    String subject, tier;
    List<Question> questions;
    int currentIndex = 0;
    int score = 0;
    int questionCount = 10;
    boolean answered = false;

    CountDownTimer countDownTimer;
    int timeLimit;
    int timeLeft;

    Handler idleHandler = new Handler();
    Runnable idleRunnable;
    BroadcastReceiver idleReceiver;
    BroadcastReceiver batteryReceiver;

    static final String ACTION_SESSION_IDLE = "com.eduquiz.SESSION_IDLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvSubjectTier = findViewById(R.id.tvSubjectTier);
        tvQuestionNum = findViewById(R.id.tvQuestionNum);
        tvScore = findViewById(R.id.tvScore);
        tvTimer = findViewById(R.id.tvTimer);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvExplanation = findViewById(R.id.tvExplanation);
        btnA = findViewById(R.id.btnA);
        btnB = findViewById(R.id.btnB);
        btnC = findViewById(R.id.btnC);
        btnD = findViewById(R.id.btnD);
        progressBar = findViewById(R.id.progressBar);

        subject = getIntent().getStringExtra("subject");
        tier = getIntent().getStringExtra("tier");

        SharedPreferences prefs = getSharedPreferences("EduQuizPrefs", MODE_PRIVATE);
        questionCount = prefs.getInt("question_count", 10);
        boolean showExplanations = prefs.getBoolean("show_explanations", true);

        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt("currentIndex", 0);
            score = savedInstanceState.getInt("score", 0);
        }

        timeLimit = tier.equals(getString(R.string.foundation)) ? 25 : 15;

        questions = QuestionBank.getQuestions(this, subject, tier);

        if (questions.size() > questionCount) {
            questions = questions.subList(0, questionCount);
        }

        tvSubjectTier.setText(subject + " - " + tier);
        progressBar.setMax(questions.size());

        setupIdleReceiver();
        setupBatteryReceiver();

        loadQuestion();
    }

    void loadQuestion() {
        if (currentIndex >= questions.size()) {
            goToResults();
            return;
        }

        answered = false;
        resetIdleTimer();

        Question q = questions.get(currentIndex);
        tvQuestionNum.setText(getString(R.string.question_counter, currentIndex + 1, questions.size()));
        tvScore.setText(getString(R.string.score_label, score));
        tvQuestion.setText(q.getQuestionText());
        tvExplanation.setVisibility(android.view.View.GONE);
        progressBar.setProgress(currentIndex);

        String[] opts = q.getOptions();
        btnA.setText("A) " + opts[0]);
        btnB.setText("B) " + opts[1]);
        btnC.setText("C) " + opts[2]);
        btnD.setText("D) " + opts[3]);

        resetButtonColors();
        enableButtons(true);
        startTimer();

        btnA.setOnClickListener(v -> handleAnswer(0));
        btnB.setOnClickListener(v -> handleAnswer(1));
        btnC.setOnClickListener(v -> handleAnswer(2));
        btnD.setOnClickListener(v -> handleAnswer(3));
    }

    void handleAnswer(int selected) {
        if (answered) return;
        answered = true;
        stopTimer();
        resetIdleTimer();

        Question q = questions.get(currentIndex);
        q.setPlayerAnswerIndex(selected);
        int correct = q.getCorrectIndex();

        enableButtons(false);
        highlightAnswers(selected, correct);

        // Scoring
        if (selected == correct) {
            if (tier.equals(getString(R.string.foundation))) {
                score += (timeLeft > 5) ? 10 : 5;
            } else {
                score += (timeLeft > 5) ? 15 : 8;
            }
        }

        tvScore.setText(getString(R.string.score_label, score));

        SharedPreferences prefs = getSharedPreferences("EduQuizPrefs", MODE_PRIVATE);
        boolean showExplanations = prefs.getBoolean("show_explanations", true);
        if (showExplanations) {
            String label = (selected == correct) ? getString(R.string.correct) : getString(R.string.wrong);
            tvExplanation.setText(label + " " + q.getExplanation());
            tvExplanation.setVisibility(android.view.View.VISIBLE);
        }

        new Handler().postDelayed(() -> {
            currentIndex++;
            loadQuestion();
        }, 1500);
    }

    void timerExpired() {
        if (answered) return;
        answered = true;

        Question q = questions.get(currentIndex);
        q.setPlayerAnswerIndex(-1);

        enableButtons(false);
        highlightAnswers(-1, q.getCorrectIndex());

        // Penalty
        if (tier.equals(getString(R.string.foundation))) {
            score -= 2;
        } else {
            score -= 5;
        }
        tvScore.setText(getString(R.string.score_label, score));

        SharedPreferences prefs = getSharedPreferences("EduQuizPrefs", MODE_PRIVATE);
        if (prefs.getBoolean("show_explanations", true)) {
            tvExplanation.setText("Time's up! " + q.getExplanation());
            tvExplanation.setVisibility(android.view.View.VISIBLE);
        }

        new Handler().postDelayed(() -> {
            currentIndex++;
            loadQuestion();
        }, 1500);
    }

    void startTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        timeLeft = timeLimit;
        tvTimer.setText(getString(R.string.time_left, timeLeft));

        countDownTimer = new CountDownTimer(timeLimit * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = (int) (millisUntilFinished / 1000);
                tvTimer.setText(getString(R.string.time_left, timeLeft));
            }

            @Override
            public void onFinish() {
                timeLeft = 0;
                tvTimer.setText(getString(R.string.time_left, 0));
                timerExpired();
            }
        }.start();
    }

    void stopTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
    }

    void highlightAnswers(int selected, int correct) {
        Button[] btns = {btnA, btnB, btnC, btnD};
        btns[correct].setBackgroundColor(Color.parseColor("#4CAF50"));
        if (selected != -1 && selected != correct) {
            btns[selected].setBackgroundColor(Color.parseColor("#F44336"));
        }
    }

    void resetButtonColors() {
        btnA.setBackgroundColor(Color.LTGRAY);
        btnB.setBackgroundColor(Color.LTGRAY);
        btnC.setBackgroundColor(Color.LTGRAY);
        btnD.setBackgroundColor(Color.LTGRAY);
    }

    void enableButtons(boolean enabled) {
        btnA.setEnabled(enabled);
        btnB.setEnabled(enabled);
        btnC.setEnabled(enabled);
        btnD.setEnabled(enabled);
    }

    void goToResults() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("subject", subject);
        intent.putExtra("tier", tier);
        intent.putExtra("score", score);
        intent.putExtra("total", questions.size());

        int[] playerAnswers = new int[questions.size()];
        int[] correctAnswers = new int[questions.size()];
        for (int i = 0; i < questions.size(); i++) {
            playerAnswers[i] = questions.get(i).getPlayerAnswerIndex();
            correctAnswers[i] = questions.get(i).getCorrectIndex();
        }
        intent.putExtra("player_answers", playerAnswers);
        intent.putExtra("correct_answers", correctAnswers);
        startActivity(intent);
        finish();
    }

    void setupIdleReceiver() {
        idleReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!answered) {
                    new AlertDialog.Builder(QuizActivity.this)
                            .setMessage(getString(R.string.still_there))
                            .setPositiveButton("Yes", null)
                            .show();
                }
            }
        };
        registerReceiver(idleReceiver, new IntentFilter(ACTION_SESSION_IDLE), Context.RECEIVER_EXPORTED);
        resetIdleTimer();
    }

    void resetIdleTimer() {
        if (idleRunnable != null) idleHandler.removeCallbacks(idleRunnable);
        idleRunnable = () -> sendBroadcast(new Intent(ACTION_SESSION_IDLE));
        idleHandler.postDelayed(idleRunnable, 60000);
    }

    void setupBatteryReceiver() {
        batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getSharedPreferences("EduQuizPrefs", MODE_PRIVATE).edit()
                        .putString("last_subject", subject)
                        .apply();
                Toast.makeText(QuizActivity.this, getString(R.string.low_battery), Toast.LENGTH_LONG).show();
            }
        };
        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!answered && currentIndex < questions.size()) {
            startTimer();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentIndex", currentIndex);
        outState.putInt("score", score);
    }


    public void onBackPressedDispatcher() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.quit_quiz))
                .setPositiveButton(getString(R.string.yes), (d, w) -> {
                    stopTimer();
                    finish();
                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
        if (idleRunnable != null) idleHandler.removeCallbacks(idleRunnable);
        unregisterReceiver(idleReceiver);
        unregisterReceiver(batteryReceiver);
    }
}
