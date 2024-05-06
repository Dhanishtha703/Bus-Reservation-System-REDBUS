package com.mohit.brs.service;

import com.mohit.brs.model.bus.Trip;
import com.mohit.brs.model.bus.TripSchedule;
import com.mohit.brs.model.request.TripScheduleDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TripScheduleService {

  void  addTripScheduleById(TripScheduleDto tripScheduleDto, Trip trip);

  List<TripSchedule> getTripScheduleListByDate(String tripDate);
}
