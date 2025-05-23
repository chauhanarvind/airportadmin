package com.airport.admin.airport_admin.repositories;

import com.airport.admin.airport_admin.models.StaffingRequest;
import com.airport.admin.airport_admin.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface StaffingRequestRepository
        extends JpaRepository<StaffingRequest, Long>,
        JpaSpecificationExecutor<StaffingRequest> {

    List<StaffingRequest> findByManagerId(Long id);

    List<StaffingRequest> findAllByOrderByCreatedAtDesc();

    Page<StaffingRequest> findAllByOrderByCreatedAtDesc(Pageable pageable);
}