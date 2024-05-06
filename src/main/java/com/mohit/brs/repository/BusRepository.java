package com.mohit.brs.repository;

import com.mohit.brs.model.bus.Bus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface BusRepository extends JpaRepository<Bus, Long> {

    Bus getById(long busId);

    Bus findByCode(String code);
}
