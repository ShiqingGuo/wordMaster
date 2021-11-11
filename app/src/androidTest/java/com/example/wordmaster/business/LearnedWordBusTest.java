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

public class LearnedWordBusTest {
    private Context context;
    private LearnedWordBus learnedWordBus;
    private UserBus userBus;
    private LearningWordBus learningWordBus;

    @Before
    public void setUp() throws Exception {
        context= ApplicationProvider.getApplicationContext();
        learnedWordBus=new LearnedWordBus(context);
        learnedWordBus.clear();
        learningWordBus=new LearningWordBus(context);
        learningWordBus.clear();
        userBus=new UserBus(context);
        userBus.clearAllUsers();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGeneral() throws DuplicateException, InvalidFormatException {
        LearnedWord learnedWord;
        List<LearnedWord> learnedWordList;
        User user=new User("user","123456");
        User user1=new User("user1","123456");
        userBus.insert(user);
        userBus.insert(user1);
        boolean success;
        List<String> unlearnedWordList;

        success= learnedWordBus.addWord("one","user",LearnedWordBus.FAMILIAR);
        assertTrue(success);
        learnedWord=learnedWordBus.getLearnedWord("one","user");
        assertEquals(4, learnedWord.getFamiliarPoint());
        assertEquals("one", learnedWord.getWord());
        assertEquals("user", learnedWord.getUserID());
        success= learnedWordBus.addWord("two","user",LearnedWordBus.NOT_SURE);
        assertTrue(success);
        learnedWord=learnedWordBus.getLearnedWord("two","user");
        assertEquals(2, learnedWord.getFamiliarPoint());
        assertEquals("two", learnedWord.getWord());
        assertEquals("user", learnedWord.getUserID());
        success= learnedWordBus.addWord("one","user1",LearnedWordBus.UNFAMILIAR);
        assertTrue(success);
        learnedWord=learnedWordBus.getLearnedWord("one","user1");
        assertEquals(0, learnedWord.getFamiliarPoint());
        assertEquals("one", learnedWord.getWord());
        assertEquals("user1", learnedWord.getUserID());

        learnedWordList=learnedWordBus.getLearnedWordByUser("user",1,false,false);
        assertEquals(1,learnedWordList.size());
        learnedWord=learnedWordList.get(0);
        assertEquals(4, learnedWord.getFamiliarPoint());
        assertEquals("one", learnedWord.getWord());
        assertEquals("user", learnedWord.getUserID());
        learnedWordList=learnedWordBus.getLearnedWordByUser("user",true);
        learnedWord=learnedWordList.get(0);
        assertEquals(2, learnedWord.getFamiliarPoint());
        assertEquals("two", learnedWord.getWord());
        assertEquals("user", learnedWord.getUserID());
        learnedWordList=learnedWordBus.getLearnedWordByUser("user1",false);
        assertEquals(1,learnedWordList.size());
        learnedWord=learnedWordList.get(0);
        assertEquals(0, learnedWord.getFamiliarPoint());
        assertEquals("one", learnedWord.getWord());
        assertEquals("user1", learnedWord.getUserID());

        unlearnedWordList=learnedWordBus.getUnlearnedWordByUser("user",50,false);
        assertEquals(50, unlearnedWordList.size());
        unlearnedWordList=learnedWordBus.getUnlearnedWordByUser("user");
        assertFalse(unlearnedWordList.contains("one"));
        assertFalse(unlearnedWordList.contains("two"));
        assertTrue(unlearnedWordList.contains("three"));

        success=learnedWordBus.updateFamiliarPoint("one","user",LearnedWordBus.FAMILIAR);
        assertTrue(success);
        learnedWord=learnedWordBus.getLearnedWord("one","user");
        assertEquals(5, learnedWord.getFamiliarPoint());
        learnedWord=learnedWordBus.getLearnedWord("one","user1");
        assertEquals(0, learnedWord.getFamiliarPoint());
        success=learnedWordBus.updateFamiliarPoint("two","user",LearnedWordBus.NOT_SURE);
        assertTrue(success);
        learnedWord=learnedWordBus.getLearnedWord("two","user");
        assertEquals(2, learnedWord.getFamiliarPoint());
        success=learnedWordBus.updateFamiliarPoint("two","user",LearnedWordBus.UNFAMILIAR);
        assertTrue(success);
        learnedWord=learnedWordBus.getLearnedWord("two","user");
        assertEquals(1, learnedWord.getFamiliarPoint());
        learnedWordBus.clear();
        learnedWord=learnedWordBus.getLearnedWord("two","user");
        assertNull(learnedWord);
    }

    @Test
    public void testExclude() throws DuplicateException, InvalidFormatException {
        User user1;
        List<LearnedWord> learnedWordList;
        List<String> unlearnedWordList;
        user1=new User("user1","123456");
        userBus.insert(user1);
        learnedWordBus.addWord("one","user1",LearnedWordBus.FAMILIAR);
        learnedWordBus.addWord("two","user1",LearnedWordBus.FAMILIAR);
        learningWordBus.insertLearningWord("one","user1", LearningWord.REVIEW_WORD);

        learnedWordList=learnedWordBus.getLearnedWordByUser("user1",2,true,false);
        assertEquals(2, learnedWordList.size());
        learnedWordList=learnedWordBus.getLearnedWordByUser("user1",2,true,true);
        assertEquals(1, learnedWordList.size());

        unlearnedWordList=learnedWordBus.getUnlearnedWordByUser("user1");
        assertTrue(unlearnedWordList.contains("three"));
        learningWordBus.insertLearningWord("three","user1", LearningWord.NEW_WORD);
        unlearnedWordList=learnedWordBus.getUnlearnedWordByUser("user1",10000,true);
        assertFalse(unlearnedWordList.contains("three"));
    }
}