package com.mohit.brs.service;

import com.mohit.brs.model.bus.Ticket;
import com.mohit.brs.model.bus.TripSchedule;
import com.mohit.brs.model.request.TicketDto;
import com.mohit.brs.model.user.User;
import org.springframework.stereotype.Service;

@Service
public interface TicketService {

    Ticket bookTicket(TicketDto ticketDto, User passenger);
}
