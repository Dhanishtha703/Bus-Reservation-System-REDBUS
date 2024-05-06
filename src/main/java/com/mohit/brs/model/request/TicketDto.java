package com.mohit.brs.model.request;

import com.mohit.brs.model.bus.TripSchedule;
import com.mohit.brs.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDto {

    private String seatNumber;

    private Boolean cancellable;

    private String journeyDate;

    private User passenger;

    private TripSchedule tripSchedule;

}
