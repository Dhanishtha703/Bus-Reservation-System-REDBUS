package com.mohit.brs.service;

import com.mohit.brs.model.bus.Stop;
import com.mohit.brs.model.bus.Trip;
import com.mohit.brs.model.bus.Agency;
import com.mohit.brs.model.bus.Bus;
import com.mohit.brs.model.request.TripDto;
import com.mohit.brs.repository.AgencyRepository;
import com.mohit.brs.repository.BusRepository;
import com.mohit.brs.repository.StopRepository;
import com.mohit.brs.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripServiceImpl implements TripService{

    @Autowired
    BusRepository busRepository;

    @Autowired
    AgencyRepository agencyRepository;

    @Autowired
    StopRepository stopRepository;

    @Autowired
    private TripRepository tripRepository;

    @Override
    public void addTrip(TripDto tripDto) {

        Trip trip = new Trip();

        String sourceStopCode = tripDto.getSourceStopCode();
        String destinationStopCode = tripDto.getDestinationStopCode();
        String busCode = tripDto.getBusCode();
        String agencyCode = tripDto.getAgencyCode();

        String sourceStopName = tripDto.getSourceStopName();
        String destinationStopName = tripDto.getDestinationStopName();

        Agency agency = agencyRepository.findByCode(agencyCode);
        Bus bus = busRepository.findByCode(busCode);
        Stop source = stopRepository.findByName(sourceStopName);
        Stop destination = stopRepository.findByName(destinationStopName);

        trip.setAgency(agency);
        trip.setBus(bus);
        trip.setSourceStop(source);
        trip.setDestinationStop(destination);
        trip.setFare(tripDto.getFare());
        trip.setJourneyTime(tripDto.getJourneyTime());

        tripRepository.saveAndFlush(trip);
    }

    @Override
    public List<Trip> getTripBetweenTwoStops(Long sourceId, Long destinationId) {
        return tripRepository.findTripBetweenTwoStops(sourceId, destinationId);
    }
}
