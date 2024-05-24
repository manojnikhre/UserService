package com.user.userservice.security.services;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.user.userservice.models.User;
import com.user.userservice.repositories.UserRepository;
import com.user.userservice.security.models.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@JsonDeserialize
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    public CustomUserDetailsService(){

    }

    
    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optinalUser = userRepository.findByEmail(username);
        if(optinalUser.isEmpty()){
            throw new UsernameNotFoundException("User with email "+username+" does not exist");
        }
        return new CustomUserDetails(optinalUser.get());
    }
}
