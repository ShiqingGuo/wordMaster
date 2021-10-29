package com.example.wordmaster.model;

import androidx.annotation.Nullable;

public class Dictionary {
    private String word;
    private String definition;

    public Dictionary(String word, String definition) {
        this.word = word;
        this.definition = definition;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    @Override
    public boolean equals(@Nullable @org.jetbrains.annotations.Nullable Object obj) {
        boolean result=false;
        if (obj instanceof Dictionary){
            Dictionary dictionary=(Dictionary)obj;
            result=this.word.equals(dictionary.word);
        }
        return result;
    }
}
