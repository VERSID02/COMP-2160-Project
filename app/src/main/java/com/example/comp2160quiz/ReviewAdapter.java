package com.example.comp2160quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Question> questions;
    private Context context;

    public ReviewAdapter(Context context, List<Question> questions) {
        this.context = context;
        this.questions = questions;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Question q = questions.get(position);
        String[] opts = q.getOptions();
        int playerIdx = q.getPlayerAnswerIndex();
        int correctIdx = q.getCorrectIndex();

        holder.tvReviewQuestion.setText((position + 1) + ". " + q.getQuestionText());

        String playerAnswerText = (playerIdx == -1) ? "No answer (timed out)" : opts[playerIdx];
        holder.tvYourAnswer.setText("Your answer: " + playerAnswerText);
        holder.tvCorrectAnswer.setText("Correct answer: " + opts[correctIdx]);
        holder.tvReviewExplanation.setText(q.getExplanation());
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tvReviewQuestion, tvYourAnswer, tvCorrectAnswer, tvReviewExplanation;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReviewQuestion = itemView.findViewById(R.id.tvReviewQuestion);
            tvYourAnswer = itemView.findViewById(R.id.tvYourAnswer);
            tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
            tvReviewExplanation = itemView.findViewById(R.id.tvReviewExplanation);
        }
    }
}
