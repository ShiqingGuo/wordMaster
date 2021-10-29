package com.example.wordmaster.model;

import androidx.annotation.Nullable;

public class FrequentWord {
    private String word;
    private int frequencyRank;

    public FrequentWord(String word, int frequencyRank) {
        this.word = word;
        this.frequencyRank = frequencyRank;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getFrequencyRank() {
        return frequencyRank;
    }

    public void setFrequencyRank(int frequencyRank) {
        this.frequencyRank = frequencyRank;
    }

    @Override
    public boolean equals(@Nullable @org.jetbrains.annotations.Nullable Object obj) {
        boolean result=false;
        if (obj instanceof FrequentWord){
            FrequentWord frequentWord=(FrequentWord)obj;
            result=this.word.equals(frequentWord.word);
        }
        return result;
    }
}
