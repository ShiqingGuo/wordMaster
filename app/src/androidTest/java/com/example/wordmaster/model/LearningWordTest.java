package com.example.wordmaster.model;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.example.wordmaster.business.UserBus;

import org.junit.Before;
import org.junit.Test;

import java.util.ConcurrentModificationException;

import static org.junit.Assert.*;

public class LearningWordTest {
    UserBus userBus;
    Context context;

    @Before
    public void setUp() throws Exception {
        context= ApplicationProvider.getApplicationContext();
        userBus=new UserBus(context);

        userBus.clearAllUsers();
        User user1,user2;
        user1=new User("user1","123456");
        user2=new User("user2","123456");
        userBus.insert(user1);
        userBus.insert(user2);
    }

    @Test
    public void testEquals() {
        LearningWord learningWord1,learningWord2;
        learningWord1=new LearningWord("one","user1",LearningWord.NEW_WORD);
        learningWord2=new LearningWord("one","user1",LearningWord.REVIEW_WORD);
        assertEquals(learningWord1,learningWord2);
        learningWord2=new LearningWord("one","user2",LearningWord.NEW_WORD);
        assertNotEquals(learningWord1,learningWord2);
        learningWord2=new LearningWord("two","user1",LearningWord.NEW_WORD);
        assertNotEquals(learningWord1,learningWord2);
        assertNotEquals(learningWord1,"one");
    }
}