package com.example.wordmaster.model;

public class UserTodayWord {
    private String word;
    private String userID;
    private int wordOrder;

    public UserTodayWord(String word, String userID, int wordOrder) {
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
