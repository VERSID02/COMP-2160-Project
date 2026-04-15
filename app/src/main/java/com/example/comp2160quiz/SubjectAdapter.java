package com.example.comp2160quiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private List<String> subjects;
    private List<String> colors;
    private Context context;
    private OnSubjectClickListener listener;

    public interface OnSubjectClickListener {
        void onSubjectClick(String subject);
    }

    public SubjectAdapter(Context context, List<String> subjects, List<String> colors, OnSubjectClickListener listener) {
        this.context = context;
        this.subjects = subjects;
        this.colors = colors;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        String subject = subjects.get(position);
        holder.tvSubjectName.setText(subject);
        holder.tvSubjectName.setTextColor(Color.parseColor(colors.get(position)));

        SharedPreferences prefs = context.getSharedPreferences("EduQuizPrefs", Context.MODE_PRIVATE);
        int bestF = prefs.getInt("best_" + subject + "_foundation", 0);
        int bestA = prefs.getInt("best_" + subject + "_advanced", 0);
        holder.tvBestScore.setText("Best - Foundation: " + bestF + "  Advanced: " + bestA);

        holder.itemView.setOnClickListener(v -> listener.onSubjectClick(subject));
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubjectName, tvBestScore;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubjectName = itemView.findViewById(R.id.tvSubjectName);
            tvBestScore = itemView.findViewById(R.id.tvBestScore);
        }
    }
}
