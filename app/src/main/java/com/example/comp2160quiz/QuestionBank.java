package com.example.comp2160quiz;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {

    public static List<Question> getQuestions(Context context, String subject, String tier) {
        List<Question> questions = new ArrayList<>();

        String[] questionTexts;
        String[] optionsA;
        String[] optionsB;
        String[] optionsC;
        String[] optionsD;
        int[] answers;
        String[] explanations;

        if (subject.equals(context.getString(R.string.mathematics))) {
            if (tier.equals(context.getString(R.string.foundation))) {
                questionTexts = context.getResources().getStringArray(R.array.math_foundation_questions);
                optionsA = context.getResources().getStringArray(R.array.math_foundation_a);
                optionsB = context.getResources().getStringArray(R.array.math_foundation_b);
                optionsC = context.getResources().getStringArray(R.array.math_foundation_c);
                optionsD = context.getResources().getStringArray(R.array.math_foundation_d);
                answers = context.getResources().getIntArray(R.array.math_foundation_answers);
                explanations = context.getResources().getStringArray(R.array.math_foundation_explanations);
            } else {
                questionTexts = context.getResources().getStringArray(R.array.math_advanced_questions);
                optionsA = context.getResources().getStringArray(R.array.math_advanced_a);
                optionsB = context.getResources().getStringArray(R.array.math_advanced_b);
                optionsC = context.getResources().getStringArray(R.array.math_advanced_c);
                optionsD = context.getResources().getStringArray(R.array.math_advanced_d);
                answers = context.getResources().getIntArray(R.array.math_advanced_answers);
                explanations = context.getResources().getStringArray(R.array.math_advanced_explanations);
            }
        } else if (subject.equals(context.getString(R.string.science))) {
            if (tier.equals(context.getString(R.string.foundation))) {
                questionTexts = context.getResources().getStringArray(R.array.science_foundation_questions);
                optionsA = context.getResources().getStringArray(R.array.science_foundation_a);
                optionsB = context.getResources().getStringArray(R.array.science_foundation_b);
                optionsC = context.getResources().getStringArray(R.array.science_foundation_c);
                optionsD = context.getResources().getStringArray(R.array.science_foundation_d);
                answers = context.getResources().getIntArray(R.array.science_foundation_answers);
                explanations = context.getResources().getStringArray(R.array.science_foundation_explanations);
            } else {
                questionTexts = context.getResources().getStringArray(R.array.science_advanced_questions);
                optionsA = context.getResources().getStringArray(R.array.science_advanced_a);
                optionsB = context.getResources().getStringArray(R.array.science_advanced_b);
                optionsC = context.getResources().getStringArray(R.array.science_advanced_c);
                optionsD = context.getResources().getStringArray(R.array.science_advanced_d);
                answers = context.getResources().getIntArray(R.array.science_advanced_answers);
                explanations = context.getResources().getStringArray(R.array.science_advanced_explanations);
            }
        } else if (subject.equals(context.getString(R.string.french))) {
            if (tier.equals(context.getString(R.string.foundation))) {
                questionTexts = context.getResources().getStringArray(R.array.french_foundation_questions);
                optionsA = context.getResources().getStringArray(R.array.french_foundation_a);
                optionsB = context.getResources().getStringArray(R.array.french_foundation_b);
                optionsC = context.getResources().getStringArray(R.array.french_foundation_c);
                optionsD = context.getResources().getStringArray(R.array.french_foundation_d);
                answers = context.getResources().getIntArray(R.array.french_foundation_answers);
                explanations = context.getResources().getStringArray(R.array.french_foundation_explanations);
            } else {
                questionTexts = context.getResources().getStringArray(R.array.french_advanced_questions);
                optionsA = context.getResources().getStringArray(R.array.french_advanced_a);
                optionsB = context.getResources().getStringArray(R.array.french_advanced_b);
                optionsC = context.getResources().getStringArray(R.array.french_advanced_c);
                optionsD = context.getResources().getStringArray(R.array.french_advanced_d);
                answers = context.getResources().getIntArray(R.array.french_advanced_answers);
                explanations = context.getResources().getStringArray(R.array.french_advanced_explanations);
            }
        } else {
            if (tier.equals(context.getString(R.string.foundation))) {
                questionTexts = context.getResources().getStringArray(R.array.history_foundation_questions);
                optionsA = context.getResources().getStringArray(R.array.history_foundation_a);
                optionsB = context.getResources().getStringArray(R.array.history_foundation_b);
                optionsC = context.getResources().getStringArray(R.array.history_foundation_c);
                optionsD = context.getResources().getStringArray(R.array.history_foundation_d);
                answers = context.getResources().getIntArray(R.array.history_foundation_answers);
                explanations = context.getResources().getStringArray(R.array.history_foundation_explanations);
            } else {
                questionTexts = context.getResources().getStringArray(R.array.history_advanced_questions);
                optionsA = context.getResources().getStringArray(R.array.history_advanced_a);
                optionsB = context.getResources().getStringArray(R.array.history_advanced_b);
                optionsC = context.getResources().getStringArray(R.array.history_advanced_c);
                optionsD = context.getResources().getStringArray(R.array.history_advanced_d);
                answers = context.getResources().getIntArray(R.array.history_advanced_answers);
                explanations = context.getResources().getStringArray(R.array.history_advanced_explanations);
            }
        }

        for (int i = 0; i < questionTexts.length; i++) {
            String[] opts = {optionsA[i], optionsB[i], optionsC[i], optionsD[i]};
            questions.add(new Question(questionTexts[i], opts, answers[i], explanations[i]));
        }

        return questions;
    }
}
