package com.mohit.brs.repository;

import com.mohit.brs.model.bus.Ticket;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface TicketRepository extends JpaRepository<Ticket,Long> {

    Ticket findBySeatNumber(String seatNumber);
}
