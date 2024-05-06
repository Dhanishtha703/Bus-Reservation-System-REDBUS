package com.mohit.brs.model.bus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "trip_schedule")
public class TripSchedule {

    @Id
    @Column(name = "trip_schedule_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripScheduleId;

    @Column(name = "available_seats")
    private Integer availableSeats;

    @Column(name = "trip_date")
    private String tripDate;

//    @Column(name = "trip_id")
//    private Integer tripId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip tripDetail;

    @OneToMany(mappedBy = "tripSchedule", cascade = CascadeType.ALL)
    private Set<Ticket> ticketsSold;


}
