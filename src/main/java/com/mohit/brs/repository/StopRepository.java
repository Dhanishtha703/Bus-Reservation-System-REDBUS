package com.mohit.brs.repository;

import com.mohit.brs.model.bus.Stop;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface StopRepository extends JpaRepository<Stop, Long> {

    Stop getById(long stopId);

    Stop findByName(String name);

}
