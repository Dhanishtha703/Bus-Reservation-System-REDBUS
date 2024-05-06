package com.mohit.brs.service;

import com.mohit.brs.model.bus.Agency;
import com.mohit.brs.model.bus.Bus;
import com.mohit.brs.model.request.BusDto;
import com.mohit.brs.model.user.User;
import org.springframework.stereotype.Service;

@Service
public interface BusService {

    void addBus(BusDto busDto, Agency agency);

    Bus getBusById(Long busId);
}
