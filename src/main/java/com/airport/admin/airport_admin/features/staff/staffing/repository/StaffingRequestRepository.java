package com.airport.admin.airport_admin.features.staff.staffing.repository;

import com.airport.admin.airport_admin.features.staff.staffing.model.StaffingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface StaffingRequestRepository
        extends JpaRepository<StaffingRequest, Long>,
        JpaSpecificationExecutor<StaffingRequest> {

    List<StaffingRequest> findByManagerId(Long id);

}