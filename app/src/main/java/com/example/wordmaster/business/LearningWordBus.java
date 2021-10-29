package com.example.wordmaster.business;

import android.content.Context;

import com.example.wordmaster.database.Database;
import com.example.wordmaster.model.LearnedWord;

import java.util.List;

public class LearningWordBus {
    private Database database;
    private Context context;

    public LearningWordBus(Context context){
        this.context=context;
        database=new Database(context);
    }

    public boolean deleteLearningWordByUser(String userID){
        return database.deleteLearningWordByUser(userID)>=1;
    }

    public boolean insertLearningWord(String word, String userID, int wordOrder){
        return database.insertLearningWord(word, userID, wordOrder)!=-1;
    }

    public int generateLearningWord(String userID,int reviewNum,int newWordNum){
        deleteLearningWordByUser(userID);
        int wordOrder=0;
        List<LearnedWord> reviewList;
        List<String> newWordList;
        LearnedWordBus learnedWordBus=new LearnedWordBus(context);
        reviewList=learnedWordBus.getLearnedWordByUser(userID,reviewNum,true);
        newWordList=learnedWordBus.getUnlearnedWordByUser(userID,newWordNum);
        for (int i = 0; i < reviewList.size(); i++) {
            insertLearningWord(reviewList.get(i).getWord(),userID,wordOrder);
            wordOrder++;
        }
        for (int i = 0; i < newWordList.size(); i++) {
            insertLearningWord(newWordList.get(i),userID,wordOrder);
            wordOrder++;
        }
        return reviewList.size()+newWordList.size();
    }
}
