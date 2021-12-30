package com.example.wordmaster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wordmaster.model.FrequentWord;
import com.example.wordmaster.model.LearningWord;
import com.example.wordmaster.model.User;
import com.example.wordmaster.model.LearnedWord;
import com.example.wordmaster.model.UserInfo;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static Database instance;

    private static final String DATABASE_NAME = "frequent_words_db.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_FREQUENT_WORD ="frequent_word";
    private static final String TABLE_USER="user";
    private static final String COLUMN_USERID="user_id";
    private static final String COLUMN_PASSWORD="password";
    private static final String TABLE_LOCAL_INFO="local_info";
    private static final String COLUMN_ACTIVE_USER="active_user";
    private static final String TABLE_LEARNED_WORD ="learned_word";
    private static final String COLUMN_WORD="word";
    private static final String COLUMN_FAMILIAR_POINT="familiar_point";
    private static final String TABLE_LEARNING_WORD="learning_word";
    private static final String TABLE_USER_INFO="user_info";
    private static final String TABLE_DICTIONARY="dictionary";
    private static final String COLUMN_TYPE="type";
    private static final String COLUMN_WORD_GENERATED_DATE="word_generated_date";
    private static final String COLUMN_REVIEW_WORD_NUM="review_word_num";
    private static final String COLUMN_NEW_WORD_NUM="new_word_num";
    private static final String COLUMN_CURR_WORD_INDEX="curr_word_index";
    private static final String COLUMN_DEFINITION="definition";

    private Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static Database getInstance(Context context){
        if (instance==null){
            instance=new Database(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    public List<FrequentWord> getAllFrequentWords(){
        List<FrequentWord> allWords;
        allWords=new ArrayList<>();
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor = db.query(TABLE_FREQUENT_WORD, null, null, null, null, null, null);
        FrequentWord frequentWord;
        while (cursor.moveToNext()){
            frequentWord=new FrequentWord(cursor.getString(0),cursor.getInt(1));
            allWords.add(frequentWord);
        }
        return allWords;
    }

    public long insertUser(String userID,String password){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_USERID,userID);
        contentValues.put(COLUMN_PASSWORD,password);
        return db.insert(TABLE_USER,null,contentValues);
    }

    public User getUserByID(String userID){
        SQLiteDatabase db=getReadableDatabase();
        User user=null;
        String query="SELECT * FROM "+TABLE_USER+" WHERE user_id=?";
        String[] values=new String[1];
        values[0]=userID;
        Cursor cursor = db.rawQuery(query, values);
        if (cursor.moveToNext()){
            String foundUserID;
            String foundPassword;
            foundUserID= cursor.getString(0);
            foundPassword= cursor.getString(1);
            user=new User(foundUserID,foundPassword);
        }
        return user;
    }

    public int updateActiveUserID(String userID){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_ACTIVE_USER,userID);
        return db.update(TABLE_LOCAL_INFO,contentValues, null, null);
    }

    public String getActiveUserID(){
        SQLiteDatabase db=getReadableDatabase();
        String foundUserID=null;
        String query="SELECT * FROM "+TABLE_LOCAL_INFO;
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToNext()){
            foundUserID= cursor.getString(1);
        }
        return foundUserID;
    }

    public long insertUserInfo(String userID, String wordGeneratedDate, int reviewWordNum, int newWordNum,
                               int currWordIndex){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_USERID,userID);
        contentValues.put(COLUMN_WORD_GENERATED_DATE,wordGeneratedDate);
        contentValues.put(COLUMN_REVIEW_WORD_NUM,reviewWordNum);
        contentValues.put(COLUMN_NEW_WORD_NUM,newWordNum);
        contentValues.put(COLUMN_CURR_WORD_INDEX,currWordIndex);
        return db.insert(TABLE_USER_INFO,null,contentValues);
    }

    public int updateUserInfo(String userID, String wordGeneratedDate, int reviewWordNum, int newWordNum,
                              int currWordIndex){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_WORD_GENERATED_DATE,wordGeneratedDate);
        contentValues.put(COLUMN_REVIEW_WORD_NUM,reviewWordNum);
        contentValues.put(COLUMN_NEW_WORD_NUM,newWordNum);
        contentValues.put(COLUMN_CURR_WORD_INDEX,currWordIndex);
        return db.update(TABLE_USER_INFO,contentValues,COLUMN_USERID+"=?",new String[]{userID});
    }

    public int updateCurrWordIndex(String userID,int currWordIndex){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_CURR_WORD_INDEX,currWordIndex);
        return db.update(TABLE_USER_INFO,contentValues,COLUMN_USERID+"=?",new String[]{userID});
    }

    public int deleteUserInfo(String userID){
        SQLiteDatabase db=getWritableDatabase();
        return db.delete(TABLE_USER_INFO,COLUMN_USERID+"=?",new String[]{userID});
    }

    public UserInfo getUserInfo(String userID){
        SQLiteDatabase db=getReadableDatabase();
        String query="select * from "+TABLE_USER_INFO+" where "+COLUMN_USERID+" = ?";
        Cursor cursor= db.rawQuery(query,new String[]{userID});
        UserInfo result;
        String wordGeneratedDate;
        int reviewWordNum;
        int newWordNum;
        int currWordIndex;
        result=null;
        if (cursor.moveToNext()){
            wordGeneratedDate= cursor.getString(1);
            reviewWordNum=cursor.getInt(2);
            newWordNum=cursor.getInt(3);
            currWordIndex=cursor.getInt(4);
            result=new UserInfo(userID,wordGeneratedDate,reviewWordNum,newWordNum,currWordIndex);
        }
        return result;
    }


    private void clearTable(String tableName){
        SQLiteDatabase db=getWritableDatabase();
        db.delete(tableName,null,null);
    }

    public void clearTableUser(){
        clearTableLearnedWord();
        clearTableLearningWord();
        clearTable(TABLE_USER_INFO);
        updateActiveUserID(null);
        clearTable(TABLE_USER);
    }

    public void clearTableLearnedWord(){
        clearTable(TABLE_LEARNED_WORD);
    }

    public void clearTableLearningWord(){
        clearTable(TABLE_LEARNING_WORD);
    }

    public int deleteUser(String userID){
        deleteUserInfo(userID);
        deleteLearningWordByUser(userID);
        deleteLearnedWordByUser(userID);
        SQLiteDatabase db=getWritableDatabase();
        return db.delete(TABLE_USER,"user_id=?",new String[]{userID});
    }

    public long insertLearnedWord(String word,String userID, int familiarPoint){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_WORD,word);
        contentValues.put(COLUMN_USERID,userID);
        contentValues.put(COLUMN_FAMILIAR_POINT,familiarPoint);
        return db.insert(TABLE_LEARNED_WORD,null,contentValues);
    }

    public long updateLearnedWord(String word,String userID, int familiarPoint){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_WORD,word);
        contentValues.put(COLUMN_USERID,userID);
        contentValues.put(COLUMN_FAMILIAR_POINT,familiarPoint);
        String whereClause=COLUMN_USERID+"=? and "+COLUMN_WORD+"=?";
        return db.update(TABLE_LEARNED_WORD,contentValues,whereClause,new String[]{userID,word} );
    }

    public int deleteLearnedWordByUser(String userID){
        SQLiteDatabase db=getWritableDatabase();
        return db.delete(TABLE_LEARNED_WORD,COLUMN_USERID+" = ?",new String[]{userID});
    }

    public LearnedWord getLearnedWord(String word, String userID){
        SQLiteDatabase db=getReadableDatabase();
        String query;
        query="SELECT * FROM "+ TABLE_LEARNED_WORD +" WHERE "+COLUMN_USERID+" =? and "+COLUMN_WORD+"=?";

        Cursor cursor= db.rawQuery(query,new String[]{userID,word});
        int familiarPoint;
        LearnedWord learnedWord;
        learnedWord=null;
        if (cursor.moveToNext()){
            familiarPoint= cursor.getInt(2);
            learnedWord =new LearnedWord(word,userID,familiarPoint);
        }
        return learnedWord;
    }

    public List<LearnedWord> getLearnedWordByUser(String userID,int limit,boolean orderByFamiliarPoint,
                                                  boolean excludeLearningWord){
        SQLiteDatabase db=getReadableDatabase();
        String query;
        query="SELECT * FROM "+ TABLE_LEARNED_WORD +" WHERE "+COLUMN_USERID+" =?";
        String[] selectionArgs;

        if (excludeLearningWord){
            query+=" and "+COLUMN_WORD+" NOT in"+" (SELECT "+COLUMN_WORD+" FROM "+ TABLE_LEARNING_WORD +
                    " WHERE "+COLUMN_USERID+" =? )";
            selectionArgs=new String[]{userID,userID,String.valueOf(limit)};
        }else {
            selectionArgs=new String[]{userID,String.valueOf(limit)};
        }
        if (orderByFamiliarPoint){
            query+=" order by "+COLUMN_FAMILIAR_POINT;
        }
        query+=" limit ?";
        Cursor cursor= db.rawQuery(query,selectionArgs);
        String word;
        int familiarPoint;
        LearnedWord learnedWord;
        List<LearnedWord> result;
        result=new ArrayList<>();
        while (cursor.moveToNext()){
            word= cursor.getString(0);
            familiarPoint= cursor.getInt(2);
            learnedWord =new LearnedWord(word,userID,familiarPoint);
            result.add(learnedWord);
        }
        return result;
    }

    public List<LearnedWord> getLearnedWordByUser(String userID,boolean orderByFamiliarPoint){
        SQLiteDatabase db=getReadableDatabase();
        String query;
        query="SELECT * FROM "+ TABLE_LEARNED_WORD +" WHERE "+COLUMN_USERID+" =?";
        String[] selectionArgs;

        selectionArgs=new String[]{userID};

        if (orderByFamiliarPoint){
            query+=" order by "+COLUMN_FAMILIAR_POINT;
        }
        Cursor cursor= db.rawQuery(query,selectionArgs);
        String word;
        int familiarPoint;
        LearnedWord learnedWord;
        List<LearnedWord> result;
        result=new ArrayList<>();
        while (cursor.moveToNext()){
            word= cursor.getString(0);
            familiarPoint= cursor.getInt(2);
            learnedWord =new LearnedWord(word,userID,familiarPoint);
            result.add(learnedWord);
        }
        return result;
    }

    public List<String> getUnlearnedWordByUser(String userID,int limit,boolean excludeLearningWord){
        SQLiteDatabase db=getReadableDatabase();
        String[] selectionArgs;
        String query=
                "SELECT * FROM "+TABLE_FREQUENT_WORD+" WHERE "+COLUMN_WORD+" NOT in"+" (SELECT "+COLUMN_WORD+" FROM "+ TABLE_LEARNED_WORD +
                        " WHERE "+COLUMN_USERID+" =? )";
        if (excludeLearningWord){
            query+=" and "+COLUMN_WORD+" NOT in"+" (SELECT "+COLUMN_WORD+" FROM "+ TABLE_LEARNING_WORD +
                    " WHERE "+COLUMN_USERID+" =? )";
            selectionArgs=new String[]{userID,userID, String.valueOf(limit)};
        }else {
            selectionArgs=new String[]{userID,String.valueOf(limit)};
        }
        query+=" limit ?";

        Cursor cursor= db.rawQuery(query,selectionArgs);
        String word;
        List<String> result;
        result=new ArrayList<>();
        while (cursor.moveToNext()){
            word= cursor.getString(0);
            result.add(word);
        }
        return result;
    }

    public List<String> getUnlearnedWordByUser(String userID){
        SQLiteDatabase db=getReadableDatabase();
        String query=
                "SELECT * FROM "+TABLE_FREQUENT_WORD+" WHERE "+COLUMN_WORD+" NOT in"+" (SELECT "+COLUMN_WORD+" FROM "+ TABLE_LEARNED_WORD +
                        " " +
                        "WHERE "+COLUMN_USERID+" =? )";
        String[] selectionArgs;

        selectionArgs=new String[]{userID};

        Cursor cursor= db.rawQuery(query,selectionArgs);
        String word;
        List<String> result;
        result=new ArrayList<>();
        while (cursor.moveToNext()){
            word= cursor.getString(0);
            result.add(word);
        }
        return result;
    }


    public int deleteLearningWordByUser(String userID){
        SQLiteDatabase db=getWritableDatabase();
        return db.delete(TABLE_LEARNING_WORD,COLUMN_USERID+"=?",new String[]{userID});
    }

    public int deleteLearningWord(String word, String userID){
        SQLiteDatabase db=getWritableDatabase();
        return db.delete(TABLE_LEARNING_WORD,COLUMN_WORD+" =? and "+COLUMN_USERID+"=?",new String[]{word,userID});
    }



    public int updateLearningWord(String word, String userID,int type){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_TYPE,type);
        return db.update(TABLE_LEARNING_WORD,contentValues,COLUMN_WORD+" =? and "+COLUMN_USERID+"=?",
                new String[]{word,userID});
    }

    public long insertLearningWord(String word, String userID,int type){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_WORD,word);
        contentValues.put(COLUMN_USERID,userID);
        contentValues.put(COLUMN_TYPE,type);
        return db.insert(TABLE_LEARNING_WORD,null,contentValues);
    }

    public List<LearningWord> getLearningWordByUser(String userID){
        SQLiteDatabase db=getReadableDatabase();
        String query="select * from "+TABLE_LEARNING_WORD+" where "+COLUMN_USERID+" =?";
        Cursor cursor= db.rawQuery(query,new String[]{userID});
        String word;
        int type;
        List<LearningWord> result=new ArrayList<>();
        LearningWord learningWord;
        while (cursor.moveToNext()){
            word= cursor.getString(0);
            type=cursor.getInt(2);
            learningWord=new LearningWord(word,userID,type);
            result.add(learningWord);
        }
        return result;
    }

    public LearningWord getLearningWord(String word, String userID){
        SQLiteDatabase db=getReadableDatabase();
        String query="select * from "+TABLE_LEARNING_WORD+" where "+ COLUMN_WORD +" =?"+" and "+COLUMN_USERID+" =?";
        Cursor cursor= db.rawQuery(query,new String[]{word,userID});
        int type;
        LearningWord learningWord;
        learningWord=null;
        if (cursor.moveToNext()){
            type=cursor.getInt(2);
            learningWord=new LearningWord(word,userID,type);
        }
        return learningWord;
    }

    public String getDefinitionByWord(String word){
        String definition=null;
        SQLiteDatabase db=getReadableDatabase();
        String query;

        query="select * from "+TABLE_DICTIONARY+" where "+COLUMN_WORD+" = ?";
        Cursor cursor= db.rawQuery(query,new String[]{word});
        if(cursor.moveToNext()){
            definition= cursor.getString(1);
        }
        return definition;
    }

}
