package com.user.userservice.controllers;

import com.user.userservice.dtos.LoginRequestDto;
import com.user.userservice.dtos.LogoutRequestDto;
import com.user.userservice.dtos.SignUpRequestDto;
import com.user.userservice.dtos.UserDto;
import com.user.userservice.models.Token;
import com.user.userservice.models.User;
import com.user.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignUpRequestDto requestDto){
        User user = userService.signUp(
          requestDto.getEmail(),
          requestDto.getName(),
          requestDto.getPassword()
        );

        return UserDto.from(user);
    }

    @PostMapping("/login")
    public Token login(@RequestBody LoginRequestDto loginRequestDto){
        return  userService.login(
                loginRequestDto.getEmail(),
                loginRequestDto.getPassword()
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto logoutRequestDto){
        userService.logout(logoutRequestDto.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/validate/{token}")
    public UserDto validateToken(@PathVariable String token){
        User user = userService.validateToken(token);
        return UserDto.from(user);
    }

    @GetMapping("/users/{id}")
    public UserDto getUserById(@PathVariable Long id){
        return null;
    }


}
