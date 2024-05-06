package com.mohit.brs.repository;

import com.mohit.brs.model.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String username);

    User findByEmail(String email);
}
