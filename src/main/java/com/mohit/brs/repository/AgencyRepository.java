package com.mohit.brs.repository;

import com.mohit.brs.model.bus.Agency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgencyRepository extends JpaRepository<Agency, Long> {

    boolean findById(long agencyId);

    Agency findByCode(String code);
}
