package com.example.wordmaster.business;

import android.content.Context;

import com.example.wordmaster.database.Database;

public class DictionaryBus {
    private Database database;

    public DictionaryBus(Context context){
        database=Database.getInstance(context);
    }

    public String getDefinitionByWord(String word){
        return database.getDefinitionByWord(word);
    }
}
