package com.mohit.brs.repository;

import com.mohit.brs.model.bus.Trip;
import com.mohit.brs.model.request.TripDto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface TripRepository extends JpaRepository<Trip,Long> {

    @Query("SELECT e FROM Trip e WHERE e.sourceStop.stopId = :source_stop_id AND e.destinationStop.stopId = :dest_stop_id")
    List<Trip> findTripBetweenTwoStops(Long source_stop_id, Long dest_stop_id);

}
