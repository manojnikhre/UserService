package com.user.userservice.exceptions;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
