package com.example.wordmaster.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.wordmaster.R;
import com.example.wordmaster.business.UserBus;
import com.example.wordmaster.exception.DuplicateException;
import com.example.wordmaster.exception.InvalidFormatException;
import com.example.wordmaster.exception.InvalidPasswordException;
import com.example.wordmaster.exception.InvalidUserIDException;
import com.example.wordmaster.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText edttxt_userid;
    private TextInputEditText edttxt_password;
    private MaterialButton btn_login;
    private MaterialButton btn_signup;
    private UserBus userBus;
    private TextInputLayout iptlot_userid;
    private TextInputLayout iptlot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();
        setBtn_login();
        setBtn_signup();
    }

    private void initialize(){
        userBus=new UserBus(this);
        edttxt_userid=findViewById(R.id.edttxt_userid);
        edttxt_password=findViewById(R.id.edttxt_password);
        btn_login=findViewById(R.id.btn_login);
        btn_signup=findViewById(R.id.btn_signup);
        iptlot_userid=findViewById(R.id.iptlot_userid);
        iptlot_password=findViewById(R.id.iptlot_password);
    }

    private void clearError(){
        edttxt_userid.setError(null);
        edttxt_password.setError(null);
    }

    private void setBtn_signup(){
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearError();
                String userID,password;
                User user;
                userID=edttxt_userid.getText().toString();
                password=edttxt_password.getText().toString();
                user=new User(userID,password);
                try {
                    userBus.insert(user);
                    userBus.setActiveUser(user);
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                } catch (DuplicateException e) {
                    edttxt_userid.setError(e.getMessage());
                    edttxt_userid.requestFocus();
                }catch (InvalidFormatException e){
                    if (e instanceof InvalidUserIDException){
                        edttxt_userid.setError(e.getMessage());
                        edttxt_userid.requestFocus();
                    }else if (e instanceof InvalidPasswordException){
                        edttxt_password.setError(e.getMessage());
                        edttxt_password.requestFocus();
                    }
                }
            }
        });
    }

    private void setBtn_login(){
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearError();
                String userID,password;
                User user,userFound;
                userID=edttxt_userid.getText().toString();
                password=edttxt_password.getText().toString();
                user=new User(userID,password);

                userFound=userBus.getUserByID(userID);
                if (userFound!=null){
                    if (userFound.getPassword().equals(password)){
                        userBus.setActiveUser(user);
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    }else {
                        edttxt_password.setError("Password doesn't match.");
                        edttxt_password.requestFocus();
                    }
                }else {
                    edttxt_userid.setError("User id doesn't exist.");
                    edttxt_userid.requestFocus();
                }
            }
        });
    }
}