package com.mohit.brs.service;

import com.mohit.brs.model.bus.Agency;
import com.mohit.brs.model.bus.Bus;
import com.mohit.brs.model.bus.Stop;
import com.mohit.brs.model.bus.Trip;
import com.mohit.brs.model.request.BusDto;
import com.mohit.brs.model.request.TripDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TripService {

    void addTrip(TripDto tripDto);

    List<Trip> getTripBetweenTwoStops(Long sourceId, Long destinationId);
}
