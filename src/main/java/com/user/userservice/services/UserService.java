package com.user.userservice.services;


import com.user.userservice.exceptions.TokenIsNotValid;
import com.user.userservice.exceptions.UserMisMatchExcetption;
import com.user.userservice.exceptions.UserNotFoundException;
import com.user.userservice.models.Token;
import com.user.userservice.models.User;
import com.user.userservice.repositories.TokenRepository;
import com.user.userservice.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;
    private TokenRepository tokenRepository;


    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder,
                UserRepository userRepository,
                TokenRepository tokenRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    public User singUp(String email, String name, String password){
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));
        user.setEmailVerified(true);

        //save the user obj to the DB
        return userRepository.save(user);
    }

    public Token login(String email, String password) {
        Optional<User> optionalUser =  userRepository.findByEmail(email);

        if(optionalUser.isEmpty()){
            throw new UserNotFoundException("User with email  "+email+"  doesn't exist");
        }

        User user = optionalUser.get();
        if(!bCryptPasswordEncoder.matches(password,user.getHashedPassword())){
            throw new UserMisMatchExcetption("User id or password is wrong");
        }
        //Login successfull

        //Generate token
        Token token = generateToken(user);
        return tokenRepository.save(token);
    }

    private Token generateToken(User user){
        LocalDate currentDate = LocalDate.now();
        LocalDate thirtyDaysLater =  currentDate.plusDays(30);

        Date exipiryDate = Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());



        //create token
        Token token = new Token();
        token.setExpiryAt(exipiryDate);

        //Generate 128 alphanumeric string
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        token.setUser(user);
        return token;

    }

    public void logout(String tokenValue) {
        Optional<Token> optionalToekn = tokenRepository.findByValueAndDeleted(tokenValue,false);

        if(optionalToekn.isEmpty()){
            throw new TokenIsNotValid("Provided token is not correct  "+tokenValue);
        }

        Token token = optionalToekn.get();
        token.setDeleted(true);
        tokenRepository.save(token);
    }

    public User validateToken(String token) {
        Optional<Token>  isValidToken = tokenRepository.findByValueAndDeletedAndExpiryAtGreaterThan(token, false, new Date());
        if(isValidToken.isEmpty()){
            throw new TokenIsNotValid("Provided token is not correct  "+token);
        }

        return isValidToken.get().getUser();
    }
}
