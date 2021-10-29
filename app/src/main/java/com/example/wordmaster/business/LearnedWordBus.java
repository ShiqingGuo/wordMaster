package com.example.wordmaster.business;

import android.content.Context;

import com.example.wordmaster.database.Database;
import com.example.wordmaster.model.LearnedWord;

import java.util.List;

public class LearnedWordBus {
    private Database database;
    public static final int FAMILIAR=3;
    public static final int NOT_SURE=2;
    public static final int UNFAMILIAR=1;


    public LearnedWordBus(Context context){
        database=new Database(context);
    }

    public long insertLearnedWord(String word,String userID, int familiarPoint){
        return database.insertLearnedWord(word,userID,familiarPoint);
    }

    public long updateLearnedWord(String word,String userID, int familiarPoint){
        return database.updateLearnedWord(word,userID,familiarPoint);
    }

    public boolean addWord(String word,String userID,int familiarType){
        return database.insertLearnedWord(word,userID,calcNewWordFamiliarPoint(familiarType))!=-1;
    }

    public boolean updateFamiliarPoint(String word,String userID,int familiarType){
        int originalPoint=getLearnedWord(word,userID).getFamiliarPoint();
        return database.updateLearnedWord(word,userID,calcLearnedWordFamiliarPoint(familiarType,originalPoint))==1;
    }

    public LearnedWord getLearnedWord(String word, String userID){
        return database.getLearnedWord(word, userID);
    }

    public List<LearnedWord> getLearnedWordByUser(String userID,int limit,boolean orderByFamiliarPoint){
        return database.getLearnedWordByUser(userID,limit,orderByFamiliarPoint);
    }

    public List<String> getUnlearnedWordByUser(String userID,int limit){
        return database.getUnlearnedWordByUser(userID, limit);
    }

    private int calcNewWordFamiliarPoint(int type){
        int result=0;
        switch (type){
            case FAMILIAR:
                result=4;
                break;
            case NOT_SURE:
                result=2;
                break;
            case UNFAMILIAR:
                result=0;
                break;
        }
        return result;
    }

    private int calcLearnedWordFamiliarPoint(int type,int originalPoint){
        int result=originalPoint;
        switch (type){
            case FAMILIAR:
                result=Math.max(5,result+1);
                break;
            case NOT_SURE:
                break;
            case UNFAMILIAR:
                result=Math.min(0,result-1);
                break;
        }
        return result;
    }
}