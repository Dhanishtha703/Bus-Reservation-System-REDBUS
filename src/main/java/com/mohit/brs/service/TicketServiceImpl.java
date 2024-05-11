package com.mohit.brs.service;

import com.mohit.brs.model.bus.Bus;
import com.mohit.brs.model.bus.Ticket;
import com.mohit.brs.model.bus.TripSchedule;
import com.mohit.brs.model.request.TicketDto;
import com.mohit.brs.model.user.User;
import com.mohit.brs.repository.TicketRepository;
import com.mohit.brs.repository.TripScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService{

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    TripScheduleRepository tripScheduleRepository;

//    @Autowired
//    Bus bus;

    @Override
    public Ticket bookTicket(TicketDto ticketDto, User passenger ){

        Ticket ticket = new Ticket();


        ticket.setSeatNumber(ticketDto.getSeatNumber());
        ticket.setCancellable(ticketDto.getCancellable());
        ticket.setJourneyDate(ticketDto.getJourneyDate());


        // Fetch TripSchedule using the provided ID
        TripSchedule tripSchedule = tripScheduleRepository.findById(ticketDto.getTripScheduleId())
                .orElseThrow(() -> new IllegalArgumentException("Trip Schedule not found for ID: " + ticketDto.getTripScheduleId()));

        ticket.setTripSchedule(tripSchedule);
        ticket.setPassenger(passenger);

        return ticketRepository.saveAndFlush(ticket);

    }
}
