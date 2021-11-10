package com.example.wordmaster.business;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.example.wordmaster.exception.DuplicateException;
import com.example.wordmaster.exception.InvalidFormatException;
import com.example.wordmaster.model.LearnedWord;
import com.example.wordmaster.model.LearningWord;
import com.example.wordmaster.model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class LearningWordBusTest {
    private LearningWordBus learningWordBus;
    private UserBus userBus;
    private LearnedWordBus learnedWordBus;

    @Before
    public void setUp() throws Exception {
        Context context;
        context=ApplicationProvider.getApplicationContext();
        learningWordBus=new LearningWordBus(context);
        learningWordBus.clear();
        learnedWordBus=new LearnedWordBus(context);
        learnedWordBus.clear();
        userBus=new UserBus(context);
        userBus.clearAllUsers();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGeneral() throws DuplicateException, InvalidFormatException {
        boolean success;
        User user1,user2;
        LearningWord learningWord;
        LearnedWord learnedWord;
        List<LearningWord> learningWordList;
        int num;
        user1=new User("user1","123456");
        user2=new User("user2","123456");
        userBus.insert(user1);
        userBus.insert(user2);

        success= learningWordBus.insertLearningWord("one","user1",0, LearningWord.IS_NEW_WORD);
        assertTrue(success);
        learningWord=learningWordBus.getLearningWord("one","user1");
        assertEquals("one",learningWord.getWord());
        assertEquals("user1",learningWord.getUserID());
        assertEquals(0,learningWord.getWordOrder());
        assertEquals(LearningWord.IS_NEW_WORD,learningWord.getIsNewWord());

        success= learningWordBus.insertLearningWord("two","user1",1, LearningWord.IS_NEW_WORD);
        assertTrue(success);
        success= learningWordBus.insertLearningWord("one","user2",0, LearningWord.IS_NEW_WORD);
        assertTrue(success);
        learningWordList=learningWordBus.getLearningWordByUser("user1");
        assertEquals(2,learningWordList.size());
        learningWordList=learningWordBus.getLearningWordByUser("user2");
        assertEquals(1,learningWordList.size());

        //test delete
        learningWordBus.deleteLearningWordByUser("user1");
        learningWordList=learningWordBus.getLearningWordByUser("user1");
        assertEquals(0,learningWordList.size());

        //test generate learning word
        learnedWordBus.addWord("one","user1",LearnedWordBus.FAMILIAR);
        learnedWordBus.addWord("two","user1",LearnedWordBus.UNFAMILIAR);
        learningWordBus.clear();
        num= learningWordBus.generateLearningWord("user1",1,3);
        assertEquals(4,num);
        learningWord=learningWordBus.getLearningWord("one","user1");
        assertNull(learningWord);
        learningWord=learningWordBus.getLearningWord("two","user1");
        assertNotNull(learningWord);
        learningWordBus.clear();

        num= learningWordBus.generateLearningWord("user1",2,3);
        assertEquals(5,num);
        learningWordList=learningWordBus.getLearningWordByUser("user1");
        assertEquals(5,learningWordList.size());
        for (int i = 0; i < 5; i++) {
            learningWord=learningWordList.get(i);
            assertEquals(i, learningWord.getWordOrder());
            if (i<2){
                assertEquals(LearningWord.IS_REVIEW_WORD,learningWord.getIsNewWord());
            }else {
                assertEquals(LearningWord.IS_NEW_WORD,learningWord.getIsNewWord());
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
}