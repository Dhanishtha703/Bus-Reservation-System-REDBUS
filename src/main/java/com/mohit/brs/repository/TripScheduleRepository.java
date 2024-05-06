package com.mohit.brs.repository;


import com.mohit.brs.model.bus.TripSchedule;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface TripScheduleRepository extends JpaRepository<TripSchedule,Long> {

    @Query("SELECT e FROM TripSchedule e WHERE e.tripDate = :trip_date")
    List<TripSchedule> findTripSchedulesByDate(@Param("trip_date") String tripDate);


}
