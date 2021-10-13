package com.example.wordmaster.exception;

public class InvalidUserIDException extends InvalidFormatException{
    public InvalidUserIDException(String message) {
        super(message);
    }
}
