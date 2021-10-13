package com.example.wordmaster.exception;

public class InvalidPasswordException extends InvalidFormatException{
    public InvalidPasswordException(String message) {
        super(message);
    }
}
