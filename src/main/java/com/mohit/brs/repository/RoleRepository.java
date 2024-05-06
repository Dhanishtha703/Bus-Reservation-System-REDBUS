package com.mohit.brs.repository;

import com.mohit.brs.model.user.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findById(Long id);
}
