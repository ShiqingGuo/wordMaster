package com.example.wordmaster.model;

public class LearnedWord {
    private String word;
    private String userID;
    private int familiarPoint;

    public LearnedWord(String word, String userID, int familiarPoint) {
        this.word = word;
        this.userID = userID;
        this.familiarPoint = familiarPoint;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getFamiliarPoint() {
        return familiarPoint;
    }

    public void setFamiliarPoint(int familiarPoint) {
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
