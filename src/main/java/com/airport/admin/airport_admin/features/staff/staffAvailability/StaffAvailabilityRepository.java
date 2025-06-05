package com.airport.admin.airport_admin.features.staff.staffAvailability;

import com.airport.admin.airport_admin.features.Admin.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

public interface StaffAvailabilityRepository extends
        JpaRepository<StaffAvailability, Long>,
        JpaSpecificationExecutor<StaffAvailability> {

    Optional<StaffAvailability> findByUserAndDate(User user, LocalDate date);
    Optional<StaffAvailability> findByUserIdAndDate(Long userId, LocalDate date);
    List<StaffAvailability> findByUserId(Long userId);

}
