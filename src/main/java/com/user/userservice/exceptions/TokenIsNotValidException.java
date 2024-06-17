package com.user.userservice.exceptions;

public class TokenIsNotValidException extends RuntimeException{
    public TokenIsNotValidException(String message){
        super(message);
    }
}
