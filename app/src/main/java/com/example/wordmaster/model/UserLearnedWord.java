package com.example.wordmaster.model;

public class UserLearnedWord {
    private String word;
    private String userID;
    private int familiarPoint;

    public UserLearnedWord(String word, String userID, int familiarPoint) {
        this.word = word;
        this.userID = userID;
        this.familiarPoint = familiarPoint;
    }

    @Override
    public String toString() {
        return "UserLearnedWord{" +
                "word='" + word + '\'' +
                ", userID='" + userID + '\'' +
                ", familiarPoint=" + familiarPoint +
                '}';
    }
}
