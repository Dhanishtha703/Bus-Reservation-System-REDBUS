package com.mohit.brs.service;

import com.mohit.brs.model.bus.Agency;
import com.mohit.brs.model.bus.Bus;
import com.mohit.brs.model.request.BusDto;
import com.mohit.brs.model.user.User;
import com.mohit.brs.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BusServiceImpl implements BusService{

    @Autowired
    private BusRepository busRepository;


    @Override
    public void addBus(BusDto busDto, Agency agency) {

        Bus bus = new Bus();

       bus.setAgency(agency);
       bus.setCode(busDto.getCode());
       bus.setCapacity(busDto.getCapacity());
       bus.setModel(busDto.getModel());

        busRepository.saveAndFlush(bus);


    }

    @Override
    public Bus getBusById(Long busId) {
        return busRepository.getById(busId);
    }
}
