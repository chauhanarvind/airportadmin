package com.airport.admin.airport_admin.services;

import com.airport.admin.airport_admin.dto.ShiftSwapRequestDto;
import com.airport.admin.airport_admin.enums.SwapStatus;
import com.airport.admin.airport_admin.models.ShiftSwapRequest;
import com.airport.admin.airport_admin.models.User;
import com.airport.admin.airport_admin.repositories.ShiftSwapRequestRepository;
import com.airport.admin.airport_admin.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftSwapRequestService {

    private final ShiftSwapRequestRepository shiftSwapRequestRepository;
    private final UserRepository userRepository;

    // Submit a new shift swap request
    public ShiftSwapRequest submitRequest(Long requesterId, ShiftSwapRequestDto dto) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("Requester not found"));

        User requestedUser = userRepository.findById(dto.getRequestedUserId())
                .orElseThrow(() -> new RuntimeException("Requested user not found"));

        ShiftSwapRequest request = ShiftSwapRequest.builder()
                .requester(requester)
                .requestedUser(requestedUser)
                .shiftDate(dto.getShiftDate())
                .reason(dto.getReason())
                .status(SwapStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        return shiftSwapRequestRepository.save(request);
    }

    // View requests submitted by the current user
    public List<ShiftSwapRequest> getMySwapRequests(Long userId) {
        return shiftSwapRequestRepository.findByRequesterId(userId);
    }

    // View requests where the user is the one being asked to swap (optional)
    public List<ShiftSwapRequest> getRequestsTargetedToUser(Long userId) {
        return shiftSwapRequestRepository.findByRequestedUserId(userId);
    }

    // Admin: View all requests
    public List<ShiftSwapRequest> getAllRequests() {
        return shiftSwapRequestRepository.findAll();
    }

    // Admin: Approve or reject a request
    public ShiftSwapRequest updateStatus(Long requestId, SwapStatus status) {
        ShiftSwapRequest request = shiftSwapRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Swap request not found"));

        request.setStatus(status);
        return shiftSwapRequestRepository.save(request);
    }
}
