package com.example.comp2160quiz;

public class Question {
    private String questionText;
    private String[] options;
    private int correctIndex;
    private String explanation;
    private int playerAnswerIndex = -1;

    public Question(String questionText, String[] options, int correctIndex, String explanation) {
        this.questionText = questionText;
        this.options = options;
        this.correctIndex = correctIndex;
        this.explanation = explanation;
    }

    public String getQuestionText() {
        return questionText; }
    public String[] getOptions() {
        return options; }
    public int getCorrectIndex() {
        return correctIndex; }
    public String getExplanation() {
        return explanation; }
    public int getPlayerAnswerIndex() {
        return playerAnswerIndex; }
    public void setPlayerAnswerIndex(int index) {
        this.playerAnswerIndex = index; }
}
