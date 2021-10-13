package com.example.wordmaster.model;

public class Word {
    private String word;
    private int frequencyRank;
    private String definition;

    public Word(String word, int frequencyRank) {
        this.word = word;
        this.frequencyRank = frequencyRank;
        this.definition=null;
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", frequencyRank=" + frequencyRank +
                ", definition='" + definition + '\'' +
                '}';
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

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
