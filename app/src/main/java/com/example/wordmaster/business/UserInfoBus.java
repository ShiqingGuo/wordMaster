package com.example.wordmaster.business;

import android.content.Context;
import android.provider.ContactsContract;

import com.example.wordmaster.database.Database;
import com.example.wordmaster.model.UserInfo;

import java.time.LocalDate;

public class UserInfoBus {
    private Context context;
    private Database database;

    public UserInfoBus(Context context){
        this.context=context;
        database= Database.getInstance(context);
    }

    public boolean insertUserInfo(String userID, String wordGeneratedDate, int reviewWordNum, int newWordNum,
                               int currWordIndex){
        return database.insertUserInfo(userID, wordGeneratedDate, reviewWordNum, newWordNum, currWordIndex)!=-1;
    }

    public boolean updateUserInfo(String userID, String wordGeneratedDate, int reviewWordNum, int newWordNum,
                              int currWordIndex){
        return database.updateUserInfo(userID, wordGeneratedDate, reviewWordNum, newWordNum, currWordIndex)==1;
    }

    public boolean deleteUserInfo(String userID){
        return database.deleteUserInfo(userID)==1;
    }

    public UserInfo getUserInfo(String userID){
        return database.getUserInfo(userID);
    }

    public boolean updateCurrWordIndex(String userID,int currWordIndex){
        return database.updateCurrWordIndex(userID, currWordIndex)==1;
    }

    public boolean laterThanWordGeneratedDate(String userID){
        String wordGeneratedDateStr=getUserInfo(userID).getWordGeneratedDate();
        LocalDate wordGeneratedDate=LocalDate.parse(wordGeneratedDateStr);
        return LocalDate.now().isAfter(wordGeneratedDate);
    }
}
