package com.airport.admin.airport_admin.repositories;

import com.airport.admin.airport_admin.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<?> findByLocationName(String locationName);
}
