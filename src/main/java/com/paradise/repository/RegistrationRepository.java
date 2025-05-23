package com.paradise.repository;

import com.paradise.domain.entities.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends JpaRepository<EventRegistration,Long> {
}
