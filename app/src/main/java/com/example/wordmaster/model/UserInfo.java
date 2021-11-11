package com.example.wordmaster.model;

public class UserInfo {
    private String userID;
    private String wordGeneratedDate;
    private int reviewWordNum;
    private int newWordNum;
    private int currWordIndex;

    public static final int DEFAULT_REVIEW_WORD_NUM=200;
    public static final int DEFAULT_NEW_WORD_NUM=100;

    public UserInfo(String userID, String wordGeneratedDate, int reviewWordNum, int newWordNum, int currWordIndex) {
        this.userID = userID;
        this.wordGeneratedDate = wordGeneratedDate;
        this.reviewWordNum = reviewWordNum;
        this.newWordNum = newWordNum;
        this.currWordIndex = currWordIndex;
    }

    public UserInfo(String userID){
        this.userID = userID;
        this.wordGeneratedDate = null;
        this.reviewWordNum = DEFAULT_REVIEW_WORD_NUM;
        this.newWordNum = DEFAULT_NEW_WORD_NUM;
        this.currWordIndex = 0;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getWordGeneratedDate() {
        return wordGeneratedDate;
    }

    public void setWordGeneratedDate(String wordGeneratedDate) {
        this.wordGeneratedDate = wordGeneratedDate;
    }

    public int getReviewWordNum() {
        return reviewWordNum;
    }

    public void setReviewWordNum(int reviewWordNum) {
        this.reviewWordNum = reviewWordNum;
    }

    public int getNewWordNum() {
        return newWordNum;
    }

    public void setNewWordNum(int newWordNum) {
        this.newWordNum = newWordNum;
    }

    public int getCurrWordIndex() {
        return currWordIndex;
    }

    public void setCurrWordIndex(int currWordIndex) {
        this.currWordIndex = currWordIndex;
    }
}
