package com.mohit.brs.service;

import com.mohit.brs.model.user.User;
import com.mohit.brs.model.request.ProfileDTO;
import com.mohit.brs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService{

    @Autowired
    UserRepository userRepository;

    @Override
    public void updateProfile(ProfileDTO profileDTO, String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }


        user.setFirstName(profileDTO.getFirstName());
        user.setLastName(profileDTO.getLastName());
        user.setPhoneNumber(profileDTO.getPhoneNumber());

        userRepository.save(user);
    }
}
