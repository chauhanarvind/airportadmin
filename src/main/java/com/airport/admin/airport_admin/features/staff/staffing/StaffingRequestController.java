package com.airport.admin.airport_admin.features.staff.staffing;

import com.airport.admin.airport_admin.enums.RosterStatus;
import com.airport.admin.airport_admin.features.Admin.user.User;
import com.airport.admin.airport_admin.features.staff.staffing.dto.StaffingRequest.StaffingRequestCreateDto;
import com.airport.admin.airport_admin.features.staff.staffing.dto.StaffingRequest.StaffingRequestDetailDto;
import com.airport.admin.airport_admin.features.staff.staffing.dto.StaffingRequest.StaffingRequestResponseDto;
import com.airport.admin.airport_admin.features.staff.staffing.dto.StaffingRequest.StaffingRequestUpdateDto;
import com.airport.admin.airport_admin.security.SecurityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/staffing-requests")
@PreAuthorize("!hasAnyRole('Crew')")
@RequiredArgsConstructor
public class StaffingRequestController {

    @Autowired
    private StaffingRequestService staffingRequestService;

    private final SecurityService securityService;

    // 1. Submit a new staffing request
    @PostMapping("/submit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StaffingRequestResponseDto> submitRequest(
            @Valid @RequestBody StaffingRequestCreateDto dto
    ) {
        Long userId = securityService.getAuthenticatedUserId();
        StaffingRequestResponseDto response = staffingRequestService.submitRequest(userId, dto);
        return ResponseEntity.ok(response);
    }


    // 2. Get a specific request by ID (detailed view)
    @GetMapping("/{id}")
    public ResponseEntity<StaffingRequestDetailDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(staffingRequestService.getRequestById(id));
    }

    // 3. Approve/reject a request (Admin only)
    @PutMapping("/status/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestBody @Valid StaffingRequestUpdateDto updateDto
    ) {
        staffingRequestService.updateStatus(id, updateDto.getStatus());
        return ResponseEntity.ok().build();
    }

    // 4. Get filtered or paged requests
    @GetMapping("/")
    public ResponseEntity<Page<StaffingRequestResponseDto>> getRequestsFiltered(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<Long> managerId,
            @RequestParam Optional<Long> locationId,
            @RequestParam Optional<RosterStatus> status
    ) {
        Pageable pageable = PageRequest.of(
                page.orElse(0),
                size.orElse(20),
                Sort.by("createdAt").descending()
        );

        Page<StaffingRequestResponseDto> result = (managerId.isPresent() || locationId.isPresent() || status.isPresent())
                ? staffingRequestService.getFilteredRequests(managerId, locationId, status, pageable)
                : staffingRequestService.getRequestsPaged(page.orElse(0), size.orElse(20));

        return ResponseEntity.ok(result);
    }

    // 5. Get all requests submitted by a manager (self or admin)
    @GetMapping("/user/{managerId}")
    @PreAuthorize("@securityService.isOwner(#managerId, authentication) or hasRole('Admin')")
    public ResponseEntity<List<StaffingRequestResponseDto>> getByManagerId(@PathVariable Long managerId) {
        List<StaffingRequestResponseDto> requests = staffingRequestService.getRequestsByManagerId(managerId);
        return ResponseEntity.ok(requests);
    }
}
