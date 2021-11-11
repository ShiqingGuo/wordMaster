package com.example.wordmaster.business;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import androidx.test.core.app.ApplicationProvider;

import com.example.wordmaster.model.User;
import com.example.wordmaster.model.UserInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserBusTest {
    Context context;
    UserBus userBus;
    UserInfoBus userInfoBus;

    @Before
    public void setUp() throws Exception {
        context=ApplicationProvider.getApplicationContext();
        userBus=new UserBus(context);
        userBus.updateActiveUser(null);
        userBus.clearAllUsers();
        userInfoBus=new UserInfoBus(context);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGeneral(){
        User user, user1,user2;
        UserInfo userInfo;
        boolean executeSuccess=false;
        user1=new User("user1","password1");
        assertNull(userBus.getUserByID("user1"));
        try {
            executeSuccess=userBus.insert(user1);
            assertTrue(executeSuccess);
        } catch (Exception e) {
            fail();
        }
        user=userBus.getUserByID("user1");
        assertNotNull(user);
        assertEquals("user1",user.getUserID());
        assertEquals("password1",user.getPassword());
        userInfo=userInfoBus.getUserInfo("user1");
        assertNull(userInfo.getWordGeneratedDate());
        assertEquals(UserInfo.DEFAULT_REVIEW_WORD_NUM, userInfo.getReviewWordNum());
        assertEquals(UserInfo.DEFAULT_NEW_WORD_NUM, userInfo.getNewWordNum());
        assertEquals(0, userInfo.getCurrWordIndex());

        user2=new User("user2","password2");
        assertNull(userBus.getUserByID("user2"));
        try {
            executeSuccess= userBus.insert(user2);
            assertTrue(executeSuccess);
        } catch (Exception e) {
            fail();
        }
        user=userBus.getUserByID("user2");
        assertNotNull(user);
        assertEquals("user2",user.getUserID());
        assertEquals("password2",user.getPassword());
        user=userBus.getUserByID("user1");
        assertNotNull(user);

        executeSuccess= userBus.updateActiveUser(user1);
        assertTrue(executeSuccess);
        user= userBus.getActiveUser();
        assertNotNull(user);
        assertEquals("user1",user.getUserID());


        try {
            userBus.deleteUser(user1);
        }catch (SQLiteConstraintException e){
            assertTrue(true);
        }
        executeSuccess= userBus.updateActiveUser(user2);
        assertTrue(executeSuccess);
        executeSuccess= userBus.deleteUser(user1);
        assertTrue(executeSuccess);

        user= userBus.getActiveUser();
        assertNotNull(user);
        assertEquals("user2",user.getUserID());
    }

    @Test
    public void testNull(){
        User user,user1;
        boolean executeSuccess;
        user1=new User("user1","password1");
        try {
            userBus.insert(user1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        executeSuccess= userBus.updateActiveUser(user1);
        assertTrue(executeSuccess);
        executeSuccess=userBus.updateActiveUser(null);
        assertTrue(executeSuccess);
        user=userBus.getActiveUser();
        assertNull(user);
    }

    @Test
    public void testEdge(){

    }
}