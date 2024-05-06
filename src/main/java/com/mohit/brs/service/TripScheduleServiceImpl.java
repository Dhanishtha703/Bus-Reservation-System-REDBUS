package com.mohit.brs.service;

import com.mohit.brs.model.bus.Trip;
import com.mohit.brs.model.bus.TripSchedule;
import com.mohit.brs.model.request.TripScheduleDto;
import com.mohit.brs.repository.TripRepository;
import com.mohit.brs.repository.TripScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripScheduleServiceImpl implements TripScheduleService{

    @Autowired
    TripScheduleRepository tripScheduleRepository;

    @Autowired
    TripRepository tripRepository;

    @Override
    public void addTripScheduleById(TripScheduleDto tripScheduleDto , Trip trip) {

        // Create a new TripSchedule instance
        TripSchedule tripSchedule = new TripSchedule();

        // Set the Trip object for the TripSchedule
        tripSchedule.setTripDetail(trip);

        // Set other properties for the TripSchedule
        tripSchedule.setTripDate(tripScheduleDto.getTripDate());
        //tripSchedule.setTripDetail(tripScheduleDto.getTripDetail());
        tripSchedule.setAvailableSeats(tripScheduleDto.getAvailableSeats());
        tripSchedule.setTicketsSold(tripScheduleDto.getTicketsSold());

        tripScheduleRepository.saveAndFlush(tripSchedule);


    }

    @Override
    public List<TripSchedule> getTripScheduleListByDate(String tripDate) {
        return tripScheduleRepository.findTripSchedulesByDate(tripDate);
    }
}
