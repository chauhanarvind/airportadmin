package com.airport.admin.airport_admin.features.staff.shiftCover;

import com.airport.admin.airport_admin.enums.CoverRequestStatus;
import com.airport.admin.airport_admin.features.staff.staffAvailability.StaffAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftCoverRequestRepository extends JpaRepository<ShiftCoverRequest, Long>,
        JpaSpecificationExecutor<ShiftCoverRequest> {

    List<ShiftCoverRequest> findByOriginalUserId(Long userId);

    List<ShiftCoverRequest> findByCoveringUserId(Long userId);

    List<ShiftCoverRequest> findByStatus(CoverRequestStatus status);
}
