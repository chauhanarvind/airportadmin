package com.airport.admin.airport_admin.features.staff.shiftCover;

import com.airport.admin.airport_admin.enums.CoverRequestStatus;
import com.airport.admin.airport_admin.features.Admin.user.User;
import com.airport.admin.airport_admin.features.staff.shiftCover.dto.CoverEligibilityCheckDto;
import com.airport.admin.airport_admin.features.staff.shiftCover.dto.ShiftCoverRequestCreateDto;
import com.airport.admin.airport_admin.features.staff.shiftCover.dto.ShiftCoverResponseDto;
import com.airport.admin.airport_admin.features.staff.shiftCover.service.CoverEligibilityService;
import com.airport.admin.airport_admin.features.staff.shiftCover.service.ShiftCoverRequestService;
import com.airport.admin.airport_admin.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/cover-requests")
@RequiredArgsConstructor
public class ShiftCoverRequestController {

    private final ShiftCoverRequestService coverRequestService;
    private final CoverEligibilityService coverEligibilityService;
    private final SecurityService securityService;

    // Authenticated user submits request (userId derived from session)
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ShiftCoverResponseDto> submitRequest(
            @RequestBody ShiftCoverRequestCreateDto dto
    ) {
        Long userId = securityService.getAuthenticatedUserId();

        ShiftCoverResponseDto saved = coverRequestService.submitCoverRequest(userId, dto);
        return ResponseEntity.ok(saved);
    }


    // Admin: Filtered list
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

    // Get all for logged-in user
    @GetMapping("/user/{userId}")
    @PreAuthorize("@securityService.isOwner(#userId, authentication) or hasRole('Admin')")
    public ResponseEntity<List<ShiftCoverResponseDto>> getAllUserCoverRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(coverRequestService.getAllUserCoverRequests(userId));
    }

    // Get one
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ShiftCoverResponseDto> getCoverRequest(@PathVariable Long id) {
        return ResponseEntity.ok(coverRequestService.getCoverRequestById(id));
    }

    @GetMapping("/shift/{shiftId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ShiftCoverResponseDto> getRequestByShift(@PathVariable Long shiftId) {
        Long userId = securityService.getAuthenticatedUserId();
        return ResponseEntity.ok(coverRequestService.getByUserAndShift(userId, shiftId));
    }

    // Pre-check warnings
    //for front-end user before submission
    @PostMapping("/check")
    public ResponseEntity<List<String>> checkCoverEligibility(@RequestBody CoverEligibilityCheckDto dto) {
        Long originalUserId = securityService.getAuthenticatedUserId();
        return ResponseEntity.ok(coverRequestService.getApprovalWarnings(dto, originalUserId ));
    }

    //for admin
    @GetMapping("/{id}/warnings")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<String>> getRequestWarnings(@PathVariable Long id) {
        return ResponseEntity.ok(coverRequestService.getApprovalWarnings(id));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("@shiftCoverSecurity.canModify(#id, authentication)")
    public ResponseEntity<Void> cancelRequest(@PathVariable Long id) {
        coverRequestService.cancelRequest(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> approveRequest(@PathVariable Long id) {
        coverRequestService.approveRequest(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> rejectRequest(@PathVariable Long id) {
        coverRequestService.rejectRequest(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('Admin') or @securityService.isOwnerOfCoverRequest(#id, authentication)")
    @PutMapping("/{id}/resubmit")
    public ResponseEntity<Void> resubmitRequest(@PathVariable Long id) {
        coverRequestService.resubmitRequest(id);
        return ResponseEntity.ok().build();
    }

}
