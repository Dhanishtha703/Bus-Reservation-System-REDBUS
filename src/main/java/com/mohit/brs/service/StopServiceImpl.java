package com.mohit.brs.service;

import com.mohit.brs.model.bus.Stop;
import com.mohit.brs.model.request.StopDto;
import com.mohit.brs.repository.StopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StopServiceImpl implements StopService {

    @Autowired
    private StopRepository stopRepository;

    @Override
    @Transactional
    public void addStop(StopDto stopDto) {
        if (stopDto == null) {
            // Handle null case or throw an exception
            throw new IllegalArgumentException("StopDto cannot be null");
        }

        Stop stop = new Stop();
        stop.setCode(stopDto.getCode());
        stop.setName(stopDto.getName());
        stop.setDetails(stopDto.getDetails());

        try {
            stopRepository.saveAndFlush(stop);
        } catch (Exception e) {
            // Handle the exception, log or rethrow
            // You might also want to catch more specific exceptions
            e.printStackTrace();
            throw new RuntimeException("Error occurred while adding stop: " + e.getMessage());
        }
    }

    @Override
    public Stop getStopById(Long stopId) {
        return stopRepository.getById(stopId);
    }

    @Override
    public List<Stop> getAllStops() {
        return stopRepository.findAll();
    }
}
