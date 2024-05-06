package com.mohit.brs.model.request;

import com.mohit.brs.model.bus.Ticket;
import com.mohit.brs.model.bus.Trip;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripScheduleDto {

    private Integer availableSeats;

    private String tripDate;

//    @Column(name = "trip_id")
//    private Integer tripId;

    private Trip tripDetail;

    private Set<Ticket> ticketsSold;


}
