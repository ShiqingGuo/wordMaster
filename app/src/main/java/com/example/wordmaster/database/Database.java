package com.example.wordmaster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wordmaster.model.User;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "frequent_words_db.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_FREQUENT_WORD ="frequent_word";
    private static final String TABLE_USER="user";
    private static final String COLUMN_USERID="user_id";
    private static final String COLUMN_PASSWORD="password";
    private static final String TABLE_LOCAL_INFO="local_info";
    private static final String COLUMN_ACTIVE_USER="active_user";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    public List<String> getAllWords(){
        List<String> allWords;
        allWords=new ArrayList<>();
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor = db.query(TABLE_FREQUENT_WORD, null, null, null, null, null, null);
        while (cursor.moveToNext()){
            allWords.add(cursor.getString(0));
        }
        return allWords;
    }

    public String getDefinitionByWord(String word){
        String definition="Sorry,no definition available.";
        SQLiteDatabase db=getReadableDatabase();
        String query="SELECT definition from frequent_words INNER JOIN dictionary WHERE " +
                "frequent_word.word=dictionary.word AND frequent_word.word=?";
        String[] values=new String[1];
        values[0]=word;
        Cursor cursor = db.rawQuery(query, values);
        if (cursor.moveToNext()){
            definition= cursor.getString(0);
        }
        return definition;
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

    public int setActiveUserID(String userID){
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
            foundUserID= cursor.getString(0);
        }
        return foundUserID;
    }

    private void clearTable(String tableName){
        SQLiteDatabase db=getWritableDatabase();
        db.delete(tableName,null,null);
    }

    public void clearTableUser(){
        clearTable(TABLE_USER);
    }

    public int deleteUser(String userID){
        SQLiteDatabase db=getWritableDatabase();
        return db.delete(TABLE_USER,"user_id=?",new String[]{userID});
    }
}
