package com.airport.admin.airport_admin.features.shiftCover;

import com.airport.admin.airport_admin.enums.CoverRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftCoverRequestRepository extends JpaRepository<ShiftCoverRequest, Long> {

    List<ShiftCoverRequest> findByOriginalUserId(Long userId);

    List<ShiftCoverRequest> findByCoveringUserId(Long userId);

    List<ShiftCoverRequest> findByStatus(CoverRequestStatus status);
}
