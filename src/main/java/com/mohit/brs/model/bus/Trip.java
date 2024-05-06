package com.mohit.brs.model.bus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tripId")
@Table(name = "trip")
public class Trip {

    @Id
    @Column(name = "trip_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripId;

    @Column(name = "fare")
    private Integer fare;

    @Column(name="journey_time")
    private Integer journeyTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="source_stop_id")
    private Stop sourceStop;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="dest_stop_id")
    private Stop destinationStop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id")
    private Bus bus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @OneToOne(mappedBy = "tripDetail", cascade = CascadeType.ALL)
    private TripSchedule tripSchedule;


}
