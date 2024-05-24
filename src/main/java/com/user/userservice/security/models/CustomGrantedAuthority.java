package com.user.userservice.security.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.user.userservice.models.Role;
import org.springframework.security.core.GrantedAuthority;

@JsonDeserialize
public class CustomGrantedAuthority implements GrantedAuthority {

    public CustomGrantedAuthority(){}


    private String authorities;

    public CustomGrantedAuthority(Role role){
        this.authorities = role.getValue();
    }
    @Override
    public String getAuthority() {
        return authorities;
    }
}
