package com.example.wordmaster.business;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.example.wordmaster.exception.DuplicateException;
import com.example.wordmaster.exception.InvalidFormatException;
import com.example.wordmaster.model.User;
import com.example.wordmaster.model.UserInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserInfoBusTest {
    private Context context;
    private UserInfoBus userInfoBus;
    private UserBus userBus;

    @Before
    public void setUp() throws Exception {
        context= ApplicationProvider.getApplicationContext();
        userInfoBus=new UserInfoBus(context);
        userBus=new UserBus(context);

        userBus.clearAllUsers();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void insertUserInfo() throws DuplicateException, InvalidFormatException {
        User user1;
        UserInfo userInfo;
        user1=new User("user1","123456");
        userBus.insert(user1);
        userInfo=userInfoBus.getUserInfo("user1");
        assertNotNull(userInfo);
    }

    @Test
    public void updateUserInfo() throws Exception {
        User user1;
        UserInfo userInfo;
        user1=new User("user1","123456");
        userBus.insert(user1);
        userInfoBus.updateUserInfo("user1","2000-01-01",123,234,30);
        userInfo=userInfoBus.getUserInfo("user1");
        assertEquals("user1",userInfo.getUserID());
        assertEquals("2000-01-01",userInfo.getWordGeneratedDate());
        assertEquals(123,userInfo.getReviewWordNum());
        assertEquals(234,userInfo.getNewWordNum());
        assertEquals(30,userInfo.getCurrWordIndex());
    }

    @Test
    public void deleteUserInfo() throws Exception{
        User user1;
        UserInfo userInfo;
        user1=new User("user1","123456");
        userBus.insert(user1);
        userInfo=userInfoBus.getUserInfo("user1");
        assertNotNull(userInfo);
        userInfoBus.deleteUserInfo("user1");
        userInfo=userInfoBus.getUserInfo("user1");
        assertNull(userInfo);
    }


    @Test
    public void updateCurrWordIndex() throws Exception{
        User user1;
        UserInfo userInfo;
        user1=new User("user1","123456");
        userBus.insert(user1);
        userInfoBus.updateUserInfo("user1","2000-01-01",123,234,30);
        userInfo=userInfoBus.getUserInfo("user1");
        assertEquals("user1",userInfo.getUserID());
        assertEquals("2000-01-01",userInfo.getWordGeneratedDate());
        assertEquals(123,userInfo.getReviewWordNum());
        assertEquals(234,userInfo.getNewWordNum());
        assertEquals(30,userInfo.getCurrWordIndex());

        userInfoBus.updateCurrWordIndex("user1",40);
        userInfo=userInfoBus.getUserInfo("user1");
        assertEquals("user1",userInfo.getUserID());
        assertEquals("2000-01-01",userInfo.getWordGeneratedDate());
        assertEquals(123,userInfo.getReviewWordNum());
        assertEquals(234,userInfo.getNewWordNum());
        assertEquals(40,userInfo.getCurrWordIndex());

    }
}