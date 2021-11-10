package com.example.wordmaster.business;

import android.content.Context;

import com.example.wordmaster.database.Database;
import com.example.wordmaster.model.LearnedWord;
import com.example.wordmaster.model.LearningWord;

import java.util.List;

public class LearningWordBus {
    private Database database;
    private Context context;
    private LearnedWordBus learnedWordBus;

    public LearningWordBus(Context context){
        this.context=context;
        database=Database.getInstance(context);
        learnedWordBus=new LearnedWordBus(context);
    }

    public void clear(){
        database.clearTableLearningWord();
    }

    public boolean deleteLearningWordByUser(String userID){
        return database.deleteLearningWordByUser(userID)>=1;
    }

    public boolean insertLearningWord(String word, String userID, int wordOrder,int isNewWord){
        return database.insertLearningWord(word, userID, wordOrder,isNewWord)!=-1;
    }

    //generate today's learning words for a user
    //reviewNum: the number of already learned word to generate(review old word)
    //newWordNum: the number of unlearned word to generate(learn new word)
    public int generateLearningWord(String userID,int reviewNum,int newWordNum){
        deleteLearningWordByUser(userID);
        int wordOrder=0;
        List<LearnedWord> reviewList;
        List<String> newWordList;
        LearnedWordBus learnedWordBus=new LearnedWordBus(context);
        reviewList=learnedWordBus.getLearnedWordByUser(userID,reviewNum,true);
        newWordList=learnedWordBus.getUnlearnedWordByUser(userID,newWordNum);
        for (int i = 0; i < reviewList.size(); i++) {
            insertLearningWord(reviewList.get(i).getWord(),userID,wordOrder, LearningWord.IS_REVIEW_WORD);
            wordOrder++;
        }
        for (int i = 0; i < newWordList.size(); i++) {
            insertLearningWord(newWordList.get(i),userID,wordOrder,LearningWord.IS_NEW_WORD);
            wordOrder++;
        }
        return reviewList.size()+newWordList.size();
    }

    public LearningWord getLearningWord(String word, String userID){
        return database.getLearningWord(word, userID);
    }

    public List<LearningWord> getLearningWordByUser(String userID){
        return database.getLearningWordByUser(userID);
    }

    //learn a word in learning word
    //if it's new word, insert to learned word
    //if it's a word that's already in learned word, update its familiar point
    public void learn(String word,String userID,int familiarType){
        LearningWord learningWord=getLearningWord(word,userID);

        if (learningWord!=null){
            if (learningWord.getIsNewWord()==LearningWord.IS_NEW_WORD){
                learnedWordBus.addWord(word,userID,familiarType);
            }else {
                learnedWordBus.updateFamiliarPoint(word,userID,familiarType);
            }
        }
    }
}
