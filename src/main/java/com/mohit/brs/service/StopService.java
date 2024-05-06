package com.mohit.brs.service;

import com.mohit.brs.model.bus.Stop;
import com.mohit.brs.model.request.StopDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StopService {

    void addStop(StopDto stopDto);

    Stop getStopById(Long stopId);

    List<Stop> getAllStops();
}
