package com.example.wordmaster.database;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DatabaseTest {
    private Context context;

    @Before
    public void setUp() throws Exception {
        context=ApplicationProvider.getApplicationContext();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGeneral(){
        Database database=Database.getInstance(context);
        System.out.println(database.getDefinitionByWord("is"));
    }
}