package com.airport.admin.airport_admin.repositories;

import com.airport.admin.airport_admin.models.StaffAvailability;
import com.airport.admin.airport_admin.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StaffAvailabilityRepository extends JpaRepository<StaffAvailability, Long> {
    List<StaffAvailability> findByUserAndDate(User user, LocalDate date);
}
