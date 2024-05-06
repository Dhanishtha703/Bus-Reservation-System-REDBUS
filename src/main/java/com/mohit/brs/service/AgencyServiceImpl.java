package com.mohit.brs.service;

import com.mohit.brs.model.bus.Agency;
import com.mohit.brs.model.request.AgencyDTO;
import com.mohit.brs.model.user.User;
import com.mohit.brs.repository.AgencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.DuplicateFormatFlagsException;
import java.util.Optional;

@Service
public class AgencyServiceImpl implements AgencyService{

    @Autowired
    AgencyRepository agencyRepository;

    @Override
    public void addAgency(AgencyDTO agencyDTO, User user) {

        Agency agency = new Agency();

        agency.setOwner(user);
        agency.setCode(agencyDTO.getCode());
        agency.setName(agencyDTO.getName());
        agency.setDetails(agencyDTO.getDetails());

        agencyRepository.saveAndFlush(agency);
    }

    @Override
    public Agency getAgencyById(Long agencyId) {
        Optional<?> opObj = agencyRepository.findById(agencyId);
        if(opObj.isPresent()){
           // throw new DuplicateFormatFlagsException("Agency id is already exists");
        }
        return agencyRepository.getReferenceById(agencyId);
    }
}
