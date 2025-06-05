package com.airport.admin.airport_admin.features.staff.shiftCover;

import com.airport.admin.airport_admin.enums.CoverRequestStatus;
import com.airport.admin.airport_admin.features.staff.shiftCover.dto.CoverEligibilityCheckDto;
import com.airport.admin.airport_admin.features.staff.shiftCover.dto.ShiftCoverRequestDto;
import com.airport.admin.airport_admin.features.staff.shiftCover.dto.ShiftCoverResponseDto;
import com.airport.admin.airport_admin.features.staff.shiftCover.service.CoverEligibilityService;
import com.airport.admin.airport_admin.features.staff.shiftCover.service.ShiftCoverRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/cover-requests")
@RequiredArgsConstructor
public class ShiftCoverRequestController {

    private final ShiftCoverRequestService coverRequestService;
    private final CoverEligibilityService coverEligibilityService;

    // Submit a new shift cover request, user or admin
    @PostMapping
    @PreAuthorize("#dto.originalUserId == authentication.principal.id or hasRole('Admin')")
    public ResponseEntity<ShiftCoverResponseDto> submitRequest(@RequestBody ShiftCoverRequestDto dto) {
        ShiftCoverResponseDto saved = coverRequestService.submitCoverRequest(dto);
        return ResponseEntity.ok(saved);
    }

    // Get all cover requests, admin
    @GetMapping
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Page<ShiftCoverResponseDto>> getFilteredRequests(
            @RequestParam(required = false) Long originalUserId,
            @RequestParam(required = false) Long coveringUserId,
            @RequestParam(required = false) CoverRequestStatus status,
            @RequestParam(required = false) LocalDate shiftDate,
            Pageable pageable
    ) {
        Page<ShiftCoverResponseDto> result = coverRequestService.getFilteredRequests(
                originalUserId, coveringUserId, status, shiftDate, pageable
        );
        return ResponseEntity.ok(result);
    }


    // Get all requests submitted by a specific user
    @GetMapping("/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id or hasRole('Admin')")
    public ResponseEntity<List<ShiftCoverResponseDto>> getAllUserCoverRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(coverRequestService.getAllUserCoverRequests(userId));
    }

    @GetMapping("cover-request/{id}") //get cover request by id
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ShiftCoverResponseDto> getCoverRequest(@PathVariable Long id) {
            return ResponseEntity.ok(coverRequestService.getCoverRequestById(id));
    }


    // Check warnings before submitting (frontend pre-check)
    // added pre-authorize checks in service
    @PostMapping("/check")
    public ResponseEntity<List<String>> checkCoverEligibility(@RequestBody CoverEligibilityCheckDto dto) {
        return ResponseEntity.ok(coverRequestService.getApprovalWarnings(dto));
    }

    // Check warnings for a submitted request (admin view)
    @GetMapping("/{id}/warnings")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<String>> getRequestWarnings(@PathVariable Long id) {
        return ResponseEntity.ok(coverRequestService.getApprovalWarnings(id));
    }

    // Cancel a request (by user)
    @PutMapping("/{id}/cancel")
    @PreAuthorize("@shiftCoverSecurity.canModify(#id, authentication)")
    public ResponseEntity<Void> cancelRequest(@PathVariable Long id) {
        coverRequestService.cancelRequest(id);
        return ResponseEntity.ok().build();
    }

    // Approve a request (admin action)
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> approveRequest(@PathVariable Long id) {
        coverRequestService.approveRequest(id);
        return ResponseEntity.ok().build();
    }

    // Reject a request (admin action)
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> rejectRequest(@PathVariable Long id) {
        coverRequestService.rejectRequest(id);
        return ResponseEntity.ok().build();
    }
}
