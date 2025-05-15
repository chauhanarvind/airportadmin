package com.airport.admin.airport_admin.repositories;

import com.airport.admin.airport_admin.models.ShiftSwapRequest;
import com.airport.admin.airport_admin.enums.SwapStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShiftSwapRequestRepository extends JpaRepository<ShiftSwapRequest, Long> {

    // Get all swap requests submitted by a specific user
    List<ShiftSwapRequest> findByRequesterId(Long requesterId);

    // Get all swap requests where the user is the one being asked to swap
    List<ShiftSwapRequest> findByRequestedUserId(Long requestedUserId);

    // Get all requests by status (PENDING, APPROVED, REJECTED)
    List<ShiftSwapRequest> findByStatus(SwapStatus status);

    // Get all requests for a user with a given status
    List<ShiftSwapRequest> findByRequesterIdAndStatus(Long requesterId, SwapStatus status);
}
