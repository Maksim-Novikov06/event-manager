package com.paradise.repository;

import com.paradise.domain.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    boolean existsByNameAndAddress(String name, String address);

}
