package com.example.wordmaster.model;

public class LearningWord {
    private String word;
    private String userID;
    private int wordOrder;

    public LearningWord(String word, String userID, int wordOrder) {
        this.word = word;
        this.userID = userID;
        this.wordOrder = wordOrder;
    }

    @Override
    public String toString() {
        return "UserTodayWord{" +
                "word='" + word + '\'' +
                ", userID='" + userID + '\'' +
                ", wordOrder=" + wordOrder +
                '}';
    }
}
