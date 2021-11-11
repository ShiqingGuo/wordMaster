package com.example.wordmaster.business;

import android.content.Context;

import com.example.wordmaster.database.Database;
import com.example.wordmaster.model.LearnedWord;
import com.example.wordmaster.model.LearningWord;
import com.example.wordmaster.model.UserInfo;

import java.time.LocalDate;
import java.util.List;

public class LearningWordBus {
    private Database database;
    private Context context;
    private LearnedWordBus learnedWordBus;
    private UserInfoBus userInfoBus;

    public LearningWordBus(Context context){
        this.context=context;
        database=Database.getInstance(context);
        learnedWordBus=new LearnedWordBus(context);
        userInfoBus=new UserInfoBus(context);
    }

    public void clear(){
        database.clearTableLearningWord();
    }

    public boolean deleteLearningWordByUser(String userID){
        return database.deleteLearningWordByUser(userID)>=1;
    }

    public boolean deleteLearningWord(String word, String userID){
        return database.deleteLearningWord(word,userID)==1;
    }

    public boolean updateLearningWord(String word, String userID,int type){
        return database.updateLearningWord(word,userID,type)==1;
    }

    public boolean insertLearningWord(String word, String userID,int type){
        return database.insertLearningWord(word, userID,type)!=-1;
    }

    //generate today's learning words for a user
    //reviewNum: the number of already learned word to generate(review old word)
    //newWordNum: the number of unlearned word to generate(learn new word)
    public int generateLearningWord(String userID,int reviewNum,int newWordNum){
        deleteLearningWordByUser(userID);
        List<LearnedWord> reviewList;
        List<String> newWordList;
        LearnedWordBus learnedWordBus=new LearnedWordBus(context);
        reviewList=learnedWordBus.getLearnedWordByUser(userID,reviewNum,true,false);
        newWordList=learnedWordBus.getUnlearnedWordByUser(userID,newWordNum,false);
        for (int i = 0; i < reviewList.size(); i++) {
            insertLearningWord(reviewList.get(i).getWord(),userID, LearningWord.REVIEW_WORD);
        }
        for (int i = 0; i < newWordList.size(); i++) {
            insertLearningWord(newWordList.get(i),userID,LearningWord.NEW_WORD);
        }

        userInfoBus.updateUserInfo(userID, LocalDate.now().toString(),reviewNum,newWordNum,
                0);
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
            if (learningWord.getType()==LearningWord.NEW_WORD){
                learnedWordBus.addWord(word,userID,familiarType);
            }else {
                learnedWordBus.updateFamiliarPoint(word,userID,familiarType);
            }
        }
    }


    public void updateLearningGoal(String userID,int reviewNum,int newWordNum ){
        UserInfo userInfo;
        List<LearningWord> learningWordList;
        int reviewDiff,newDiff;
        LearningWord learningWord;
        String currWord;
        int currWordIndex;

        learningWordList=getLearningWordByUser(userID);
        userInfo=userInfoBus.getUserInfo(userID);
        reviewDiff=reviewNum-userInfo.getReviewWordNum();
        newDiff=newWordNum-userInfo.getNewWordNum();
        currWordIndex=userInfo.getCurrWordIndex();
        currWord=learningWordList.get(currWordIndex).getWord();

        //delete extra words
        for (int i = learningWordList.size()-1; i > currWordIndex&&(reviewDiff<0||newDiff<0); i--) {
            learningWord=learningWordList.get(i);
            if (reviewDiff<0&&learningWord.getType()==LearningWord.REVIEW_WORD){
                deleteLearningWord(learningWord.getWord(), userID);
                reviewDiff++;
            }
            if (newDiff<0&&learningWord.getType()==LearningWord.NEW_WORD){
                deleteLearningWord(learningWord.getWord(), userID);
                newDiff++;
            }
        }
        if (reviewDiff>0){
            List<LearnedWord> learnedWordList=learnedWordBus.getLearnedWordByUser(userID,reviewDiff,true,true);
            LearnedWord learnedWord;
            for (int i = 0; i < learnedWordList.size(); i++) {
                learnedWord=learnedWordList.get(i);
                insertLearningWord(learnedWord.getWord(),userID,LearningWord.REVIEW_WORD);
            }
        }
        if (newDiff>0){
            List<String> unlearnedWordList=learnedWordBus.getUnlearnedWordByUser(userID,newDiff,true);
            String unlearnedWord;
            for (int i = 0; i < unlearnedWordList.size(); i++) {
                unlearnedWord=unlearnedWordList.get(i);
                insertLearningWord(unlearnedWord,userID,LearningWord.NEW_WORD);
            }
        }

        learningWordList=getLearningWordByUser(userID);
        currWordIndex=learningWordList.indexOf(new LearningWord(currWord,userID,-1));
        userInfoBus.updateUserInfo(userID,userInfo.getWordGeneratedDate(),reviewNum,newWordNum,currWordIndex);
    }
}
