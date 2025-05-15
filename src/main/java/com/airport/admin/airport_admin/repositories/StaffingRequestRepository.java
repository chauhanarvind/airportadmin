package com.airport.admin.airport_admin.repositories;

import com.airport.admin.airport_admin.models.StaffingRequest;
import com.airport.admin.airport_admin.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffingRequestRepository extends JpaRepository<StaffingRequest, Long> {
    List<StaffingRequest> findByManagerId(Long id);
}
