package com.mohit.brs.service;

import com.mohit.brs.model.request.ProfileDTO;
import org.springframework.stereotype.Service;

@Service
public interface ProfileService {
    void updateProfile(ProfileDTO profileDTO, String username);
}
