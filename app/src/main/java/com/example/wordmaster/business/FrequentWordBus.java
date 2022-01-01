package com.example.wordmaster.business;

import android.content.Context;

import com.example.wordmaster.database.Database;
import com.example.wordmaster.model.FrequentWord;

import java.util.List;

public class FrequentWordBus {
    private Database database;

    public FrequentWordBus(Context context){
        database=Database.getInstance(context);
    }

    public List<FrequentWord> getAllFrequentWords(){
        return database.getAllFrequentWords();
    }

    public List<FrequentWord> implicitSearch(String searchWord){
        return database.implicitSearch(searchWord);
    }

    public List<FrequentWord> getNextBatchFrequentWords(String lastWord){
        if (lastWord==null){
            return database.getFirstBatchFrequentWords();
        }else {
            return database.getNextBatchFrequentWords(lastWord);
        }
    }
}
