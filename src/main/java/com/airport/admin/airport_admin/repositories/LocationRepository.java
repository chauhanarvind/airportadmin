package com.airport.admin.airport_admin.repositories;

import com.airport.admin.airport_admin.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
