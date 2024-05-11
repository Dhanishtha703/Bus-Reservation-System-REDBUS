package com.mohit.brs.model.bus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mohit.brs.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "ticket")
public class Ticket {

    @Id
    @Column(name = "ticket_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;


    @Column(name = "seat_number", unique = true)
    private String seatNumber;

    @Column(name = "cancellable")
    private Boolean cancellable;

    @Column(name = "journey_date")
    private String journeyDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User passenger;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "trip_schedule_id")
    @JsonIgnore
    private TripSchedule tripSchedule;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return seatNumber.equals(ticket.seatNumber) &&
                journeyDate.equals(ticket.journeyDate) &&
                passenger.equals(ticket.passenger);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seatNumber, journeyDate, passenger);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", seatNumber='" + seatNumber + '\'' +
                ", cancellable=" + cancellable +
                ", journeyDate='" + journeyDate + '\'' +
                '}';
    }

}