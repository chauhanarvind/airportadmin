package com.airport.admin.airport_admin.features.staffAvailability;

import com.airport.admin.airport_admin.enums.LeaveStatus;
import com.airport.admin.airport_admin.features.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

public interface StaffAvailabilityRepository extends JpaRepository<StaffAvailability, Long> {

    Optional<StaffAvailability> findByUserAndDate(User user, LocalDate date);
    Optional<StaffAvailability> findByUserIdAndDate(Long userId, LocalDate date);
    List<StaffAvailability> findByUserId(Long userId);

}
