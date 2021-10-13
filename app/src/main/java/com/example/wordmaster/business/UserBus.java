package com.example.wordmaster.business;

import android.content.Context;

import com.example.wordmaster.database.Database;
import com.example.wordmaster.exception.DuplicateException;
import com.example.wordmaster.exception.InvalidFormatException;
import com.example.wordmaster.exception.InvalidPasswordException;
import com.example.wordmaster.exception.InvalidUserIDException;
import com.example.wordmaster.model.User;

import java.util.List;

public class UserBus {
    private List<User> userList;
    private int index;
    private Database database;
    private final int ID_MIN_LEN =3;
    private final int ID_MAX_LEN =15;
    private final int PASS_MIN_LEN =6;
    private final int PASS_MAX_LEN =20;

    public UserBus(Context context) {
        this.userList = null;
        index=0;
        database=new Database(context);
    }

    private void validateUserID(String userID) throws InvalidUserIDException {
        String message;
        message=null;
        if (userID.contains(" ")){
            message="User ID shouldn't contain space.";
        }else if (userID.length()< ID_MIN_LEN ||userID.length()> ID_MAX_LEN){
            message="Length of user ID should be between %d and %d";
            message= String.format(message, ID_MIN_LEN, ID_MAX_LEN);
        }
        if (message!=null){
            throw new InvalidUserIDException(message);
        }
    }

    private void validatePassword(String password) throws InvalidPasswordException {
        String message;
        message=null;
        if (password.contains(" ")){
            message="password shouldn't contain space.";
        }else if (password.length()< PASS_MIN_LEN ||password.length()> PASS_MAX_LEN){
            message="Length of password should be between %d and %d";
            message= String.format(message, PASS_MIN_LEN, PASS_MAX_LEN);
        }
        if (message!=null){
            throw new InvalidPasswordException(message);
        }
    }

    private boolean validate(User user) throws InvalidFormatException, DuplicateException {
        if (user==null||user.getUserID()==null||user.getPassword()==null){
            return false;
        }else {
            validateUserID(user.getUserID());
            validatePassword(user.getPassword());
            if (getUserByID(user.getUserID())!=null){
                throw new DuplicateException("This user ID already exists.");
            }
            return true;
        }
    }

    public boolean insert(User user) throws DuplicateException, InvalidFormatException {
        if (validate(user)){
            return database.insertUser(user.getUserID(), user.getPassword())!=-1;
        }
        return false;
    }

    public User getUserByID(String userID){
        if (userID!=null){
            return database.getUserByID(userID);
        }else {
            return null;
        }
    }

    public boolean setActiveUser(User user){
        if (user==null){
            return database.setActiveUserID(null)==1;
        }
        else if (user!=null&& getUserByID(user.getUserID())!=null){
            return database.setActiveUserID(user.getUserID())==1;
        }
        return false;
    }

    public User getActiveUser(){
        return getUserByID(database.getActiveUserID());
    }

    public void clearAllUsers(){
        database.clearTableUser();
    }

    public boolean deleteUser(User user){
        if (user!=null&&user.getUserID()!=null){
            return database.deleteUser(user.getUserID())==1;
        }
        return false;
    }
}
