package com.example.wordmaster.business;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.example.wordmaster.exception.DuplicateException;
import com.example.wordmaster.exception.InvalidFormatException;
import com.example.wordmaster.model.LearnedWord;
import com.example.wordmaster.model.LearningWord;
import com.example.wordmaster.model.User;
import com.example.wordmaster.model.UserInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class LearningWordBusTest {
    private LearningWordBus learningWordBus;
    private UserBus userBus;
    private LearnedWordBus learnedWordBus;
    private UserInfoBus userInfoBus;

    @Before
    public void setUp() throws Exception {
        Context context;
        context=ApplicationProvider.getApplicationContext();
        learningWordBus=new LearningWordBus(context);
        learnedWordBus=new LearnedWordBus(context);
        userBus=new UserBus(context);
        userBus.clearAllUsers();
        userInfoBus=new UserInfoBus(context);

        User user1,user2;
        user1=new User("user1","123456");
        user2=new User("user2","123456");
        userBus.insert(user1);
        userBus.insert(user2);
        learningWordBus.clear();
        learnedWordBus.clear();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGeneral() throws DuplicateException, InvalidFormatException {
        boolean success;
        LearningWord learningWord;
        LearnedWord learnedWord;
        List<LearningWord> learningWordList;
        int num;
        UserInfo userInfo;

        success= learningWordBus.insertLearningWord("one","user1", LearningWord.NEW_WORD);
        assertTrue(success);
        learningWord=learningWordBus.getLearningWord("one","user1");
        assertEquals("one",learningWord.getWord());
        assertEquals("user1",learningWord.getUserID());
        assertEquals(LearningWord.NEW_WORD,learningWord.getType());

        success= learningWordBus.insertLearningWord("two","user1", LearningWord.NEW_WORD);
        assertTrue(success);
        success= learningWordBus.insertLearningWord("one","user2", LearningWord.NEW_WORD);
        assertTrue(success);
        learningWordList=learningWordBus.getLearningWordByUser("user1");
        assertEquals(2,learningWordList.size());
        learningWordList=learningWordBus.getLearningWordByUser("user2");
        assertEquals(1,learningWordList.size());

        //test update
        success= learningWordBus.updateLearningWord("one","user1",LearningWord.REVIEW_WORD);
        assertTrue(success);
        learningWord=learningWordBus.getLearningWord("one","user1");
        assertEquals("one",learningWord.getWord());
        assertEquals("user1",learningWord.getUserID());
        assertEquals(LearningWord.REVIEW_WORD,learningWord.getType());
        //test delete
        learningWordBus.deleteLearningWordByUser("user1");
        learningWordList=learningWordBus.getLearningWordByUser("user1");
        assertEquals(0,learningWordList.size());
        learningWordBus.deleteLearningWord("one","user2");
        learningWord=learningWordBus.getLearningWord("one","user2");
        assertNull(learningWord);

        //test generate learning word
        userInfoBus.updateCurrWordIndex("user1",1);
        userInfo=userInfoBus.getUserInfo("user1");
        assertEquals(1,userInfo.getCurrWordIndex());
        learnedWordBus.addWord("one","user1",LearnedWordBus.FAMILIAR);
        learnedWordBus.addWord("two","user1",LearnedWordBus.UNFAMILIAR);
        learningWordBus.clear();
        learningWordBus.updateLearningGoal("user1",1,3);
        num= learningWordBus.generateLearningWord("user1");
        assertEquals(4,num);
        userInfo=userInfoBus.getUserInfo("user1");
        assertEquals(LocalDate.now().toString(),userInfo.getWordGeneratedDate());
        assertEquals(0,userInfo.getCurrWordIndex());
        assertEquals(1, userInfo.getReviewWordNum());
        assertEquals(3, userInfo.getNewWordNum());
        learningWord=learningWordBus.getLearningWord("one","user1");
        assertNull(learningWord);
        learningWord=learningWordBus.getLearningWord("two","user1");
        assertNotNull(learningWord);
        learningWordBus.clear();

        learningWordBus.updateLearningGoal("user1",2,3);
        num= learningWordBus.generateLearningWord("user1");
        assertEquals(5,num);
        learningWordList=learningWordBus.getLearningWordByUser("user1");
        assertEquals(5,learningWordList.size());
        for (int i = 0; i < 5; i++) {
            learningWord=learningWordList.get(i);
            if (i<2){
                assertEquals(LearningWord.REVIEW_WORD,learningWord.getType());
            }else {
                assertEquals(LearningWord.NEW_WORD,learningWord.getType());
            }
        }
        learningWord=learningWordBus.getLearningWord("one","user1");
        assertNotNull(learningWord);
        learningWord=learningWordBus.getLearningWord("two","user1");
        assertNotNull(learningWord);

        //test learn
        learningWordBus.learn("one","user1",LearnedWordBus.UNFAMILIAR);
        learnedWord=learnedWordBus.getLearnedWord("one","user1");
        assertEquals(3,learnedWord.getFamiliarPoint());
        learningWord=learningWordList.get(2);
        learningWordBus.learn(learningWord.getWord(),"user1",LearnedWordBus.FAMILIAR);
        learnedWord=learnedWordBus.getLearnedWord(learningWord.getWord(),"user1");
        assertEquals(4,learnedWord.getFamiliarPoint());
    }

    @Test
    public void testUpdateLearningGoal(){
        List<LearningWord> learningWordList;
        LearningWord learningWord;
        UserInfo userInfo;

        //shrink review and new word
        learnedWordBus.addWord("one","user1",LearnedWordBus.FAMILIAR);
        learnedWordBus.addWord("two","user1",LearnedWordBus.FAMILIAR);
        learningWordBus.updateLearningGoal("user1",2,2);
        learningWordBus.generateLearningWord("user1");
        learningWordBus.updateLearningGoal("user1",1,1);
        learningWordList=learningWordBus.getLearningWordByUser("user1");
        assertEquals(2,learningWordList.size());
        learningWord=learningWordList.get(0);
        assertEquals(LearningWord.REVIEW_WORD,learningWord.getType());
        learningWord=learningWordList.get(1);
        assertEquals(LearningWord.NEW_WORD,learningWord.getType());

        //add review and new word
        learningWordBus.updateLearningGoal("user1",2,2);
        learningWordList=learningWordBus.getLearningWordByUser("user1");
        assertEquals(4,learningWordList.size());
        learningWord=learningWordList.get(0);
        assertEquals(LearningWord.REVIEW_WORD,learningWord.getType());
        learningWord=learningWordList.get(1);
        assertEquals(LearningWord.NEW_WORD,learningWord.getType());
        learningWord=learningWordList.get(2);
        assertEquals(LearningWord.REVIEW_WORD,learningWord.getType());
        learningWord=learningWordList.get(3);
        assertEquals(LearningWord.NEW_WORD,learningWord.getType());

        //shrink new word
        learningWordBus.updateLearningGoal("user1",2,1);
        learningWordList=learningWordBus.getLearningWordByUser("user1");
        assertEquals(3,learningWordList.size());
        learningWord=learningWordList.get(0);
        assertEquals(LearningWord.REVIEW_WORD,learningWord.getType());
        learningWord=learningWordList.get(1);
        assertEquals(LearningWord.NEW_WORD,learningWord.getType());
        learningWord=learningWordList.get(2);
        assertEquals(LearningWord.REVIEW_WORD,learningWord.getType());

        //update goal after learn word
        learningWord=learningWordList.get(1);
        assertEquals(LearningWord.NEW_WORD,learningWord.getType());
        learningWordBus.learn(learningWord.getWord(),"user1",LearnedWordBus.FAMILIAR);
        learningWordBus.updateLearningGoal("user1",3,1);
        learningWordList=learningWordBus.getLearningWordByUser("user1");
        assertEquals(3,learningWordList.size());
        learningWord=learningWordList.get(0);
        assertEquals("one",learningWord.getWord());
        learningWord=learningWordList.get(1);
        assertEquals(LearningWord.NEW_WORD,learningWord.getType());
        learningWord=learningWordList.get(2);
        assertEquals("two",learningWord.getWord());

        //currWordIndex is big
        userInfoBus.updateCurrWordIndex("user1",1);
        learningWordBus.updateLearningGoal("user1",1,0);
        learningWordList=learningWordBus.getLearningWordByUser("user1");
        assertEquals(2,learningWordList.size());
        learningWord=learningWordList.get(0);
        assertEquals("one",learningWord.getWord());
        learningWord=learningWordList.get(1);
        assertEquals(LearningWord.NEW_WORD,learningWord.getType());
    }
}