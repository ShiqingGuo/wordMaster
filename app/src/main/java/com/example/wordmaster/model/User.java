package com.example.wordmaster.model;

import androidx.annotation.Nullable;

public class User {
    private String userID;
    private String password;

    public User(String userID, String password) {
        this.userID = userID;
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(@Nullable @org.jetbrains.annotations.Nullable Object obj) {
        if (obj instanceof User){
            User user=(User)obj;
            return this.userID.equals(user.getUserID());
        }
        else {
            return false;
        }
    }
}
