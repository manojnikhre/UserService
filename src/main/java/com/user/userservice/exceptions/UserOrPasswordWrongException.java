package com.user.userservice.exceptions;

public class UserOrPasswordWrongException extends RuntimeException{
    public UserOrPasswordWrongException(String message){
        super(message);
    }
}
