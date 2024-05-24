package com.user.userservice.exceptions;

public class UserMisMatchExcetption extends RuntimeException{
    public UserMisMatchExcetption(String errorMessage){
        super(errorMessage);
    }
}
