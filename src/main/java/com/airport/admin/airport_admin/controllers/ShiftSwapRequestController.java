package com.airport.admin.airport_admin.controllers;

import com.airport.admin.airport_admin.dto.ShiftSwapRequestDto;
import com.airport.admin.airport_admin.enums.SwapStatus;
import com.airport.admin.airport_admin.models.ShiftSwapRequest;
import com.airport.admin.airport_admin.security.CustomUserDetails;
import com.airport.admin.airport_admin.services.ShiftSwapRequestService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shift-swaps")
@RequiredArgsConstructor
public class ShiftSwapRequestController {

    private final ShiftSwapRequestService shiftSwapRequestService;

    // 1. Submit a shift swap request
    @PostMapping("/request")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ShiftSwapRequest> submitRequest(@RequestBody ShiftSwapRequestDto dto,
                                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        ShiftSwapRequest request = shiftSwapRequestService.submitRequest(userDetails.getId(), dto);
        return ResponseEntity.ok(request);
    }

    // 2. Get all requests submitted by current user
    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ShiftSwapRequest>> getMyRequests(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(shiftSwapRequestService.getMySwapRequests(userDetails.getId()));
    }

    // 3. Requests where current user is the requested swap partner
    @GetMapping("/to-me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ShiftSwapRequest>> getRequestsToMe(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(shiftSwapRequestService.getRequestsTargetedToUser(userDetails.getId()));
    }

    // 4. Admin: Get all swap requests
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ShiftSwapRequest>> getAll() {
        return ResponseEntity.ok(shiftSwapRequestService.getAllRequests());
    }

    // 5. Approve or reject a request
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShiftSwapRequest> updateStatus(@PathVariable Long id,
                                                         @RequestParam SwapStatus status) {
        return ResponseEntity.ok(shiftSwapRequestService.updateStatus(id, status));
    }
}
