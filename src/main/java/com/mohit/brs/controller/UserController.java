package com.mohit.brs.controller;

import com.mohit.brs.config.JwtHelper;
import com.mohit.brs.model.request.*;
import com.mohit.brs.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtHelper jwtHelper;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        if (userService.isUserExists(userRegistrationDto.getEmail())) {
            return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
        }
        userService.registerUser(userRegistrationDto);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }

    @GetMapping("/login")
    public ResponseEntity<?> generateJwtToken(@RequestBody UserLoginDto userLoginDto) throws Exception {

        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword()));

            // Set authentication in Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token and return as response
            String jwtToken = jwtHelper.generateTokenCustom(authentication);
            return ResponseEntity.ok().body("JWT Token: " + jwtToken);
        } catch (Exception e) {
            // If authentication fails, return an error response
            return ResponseEntity.status(401).body("Authentication failed: " + e.getMessage());
        }
    }


}
