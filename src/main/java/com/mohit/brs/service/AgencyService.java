package com.mohit.brs.service;

import com.mohit.brs.model.bus.Agency;
import com.mohit.brs.model.request.AgencyDTO;
import com.mohit.brs.model.user.User;
import org.springframework.stereotype.Service;

@Service
public interface AgencyService {

    void addAgency(AgencyDTO agencyDTO, User user);

    Agency getAgencyById(Long agencyId);

}
