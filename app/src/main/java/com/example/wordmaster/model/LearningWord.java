package com.example.wordmaster.model;

public class LearningWord {
    private String word;
    private String userID;
    private int wordOrder;
    private int isNewWord;

    public static final int IS_NEW_WORD=1;
    public static final int IS_REVIEW_WORD=0;

    public LearningWord(String word, String userID, int wordOrder,int isNewWord) {
        this.word = word;
        this.userID = userID;
        this.wordOrder = wordOrder;
        this.isNewWord=isNewWord;
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

    public int getWordOrder() {
        return wordOrder;
    }

    public void setWordOrder(int wordOrder) {
        this.wordOrder = wordOrder;
    }

    public int getIsNewWord() {
        return isNewWord;
    }

    public void setIsNewWord(int isNewWord) {
        this.isNewWord = isNewWord;
    }
}
