package com.mohit.brs.service;

import com.mohit.brs.model.user.User;
import com.mohit.brs.model.request.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public interface UserService extends UserDetailsService {

    void registerUser(UserRegistrationDto userRegistrationDto);

    void loginUser(String email, String password);

    boolean isUserExists(String email);

    void saveUser(User user);

    UserDetails loadUserByUsername(String email);

    User findByEmail(String username);

    Optional<User> getUserById(Long passengerId);
}
