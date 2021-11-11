package com.example.wordmaster.model;


public class LearningWord {
    private String word;
    private String userID;
    private int type;

    public static final int NEW_WORD =1;
    public static final int REVIEW_WORD =0;

    public LearningWord(String word, String userID,int type) {
        this.word = word;
        this.userID = userID;
        this.type = type;
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


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object object){
        boolean result=false;
        if (object instanceof LearningWord){
            LearningWord learningWord=(LearningWord) object;
            result=learningWord.getUserID().equals(userID)&&learningWord.getWord().equals(word);
        }
        return result;
    }
}
