package com.user.userservice.exceptions;

public class TokenIsNotValid extends RuntimeException{
    public TokenIsNotValid(String errorMessage){
        super(errorMessage);
    }
}
