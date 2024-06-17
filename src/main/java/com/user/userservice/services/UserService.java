package com.user.userservice.services;

import com.user.userservice.exceptions.TokenIsNotValidException;
import com.user.userservice.exceptions.UserNotFoundException;
import com.user.userservice.exceptions.UserOrPasswordWrongException;
import com.user.userservice.models.Token;
import com.user.userservice.models.User;
import com.user.userservice.repositories.IUserRepository;
import com.user.userservice.repositories.TokenRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private IUserRepository userRepository;

    public User signUp(String email, String name, String password){
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setHashedPassword(passwordEncoder.encode(password)); // encoding password using Spring Security BCryptPasswordEncoder
        user.setEmailVerified(true);

        //Save the user object to DB
        return userRepository.save(user);
    }

    public Token login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty()){
            throw new UserNotFoundException("User with email "+email+" not found");
        }

        User user = optionalUser.get();
        if(!passwordEncoder.matches(password,user.getHashedPassword())){
            //Throw some exception...
            throw new UserOrPasswordWrongException("User name or password is wrong...");
        }

        //Login successfull, generate Token
        Token token = generateToken(user);
        tokenRepository.save(token);
        return token;
    }

    private Token generateToken(User user){
        // Get the current date
        Date currentDate = new Date();

        // Create a Calendar instance and set the current date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // Add 30 days to the current date
        calendar.add(Calendar.DAY_OF_MONTH, 30);

        // Get the future date
        Date futureDate = calendar.getTime();


        //128 character alphanumeric string
        Token token = new Token();
        token.setExpiryAt(futureDate);
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        token.setUser(user);
        return token;
    }

    public void logout(String tokenValue){
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeleted(tokenValue, false);

        if(optionalToken.isEmpty()){
            throw new TokenIsNotValidException("Token is not valid login first");
        }

        Token token = optionalToken.get();
        token.setDeleted(true);
        tokenRepository.save(token);
    }

    public User validateToken(String token){
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedAndExpiryAtGreaterThan(token, false, new Date());

        if(optionalToken.isEmpty()){
            //throw some exception
            return null;
        }

        return optionalToken.get().getUser();
    }
}
