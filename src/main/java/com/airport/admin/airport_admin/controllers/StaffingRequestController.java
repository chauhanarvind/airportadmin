package com.airport.admin.airport_admin.controllers;

import com.airport.admin.airport_admin.dto.StaffingRequestsDto;
import com.airport.admin.airport_admin.dto.StaffingRequestsSummaryDto;
import com.airport.admin.airport_admin.enums.RosterStatus;
import com.airport.admin.airport_admin.mappers.StaffingRequestMapper;
import com.airport.admin.airport_admin.models.StaffingRequest;
import com.airport.admin.airport_admin.services.StaffingRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.airport.admin.airport_admin.security.CustomUserDetails;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/staffing-requests")
public class StaffingRequestController {

    @Autowired
    private StaffingRequestService staffingRequestService;
    @Autowired
    private StaffingRequestMapper staffingRequestMapper;
    // 1. Submit a new staffing request
    @PostMapping("/submit")
    @PreAuthorize("!hasAnyRole('Crew')")
    public ResponseEntity<StaffingRequest> submitRequest(@RequestBody StaffingRequestsDto dto) {
        StaffingRequest request = staffingRequestService.submitRequest(dto);
        return ResponseEntity.ok(request);
    }

    // 4. Get a specific request by ID
    @GetMapping("/{id}")
    @PreAuthorize("!hasAnyRole('Crew')")
    public ResponseEntity<StaffingRequest> getById(@PathVariable Long id) {
        return ResponseEntity.ok(staffingRequestService.getRequestById(id));
    }

    // 5. Approve/reject a request (admin or supervisor only)
    @PutMapping("/{id}/status") //we will just be updating the status
    @PreAuthorize("hasAnyRole('Admin' , 'Supervisor')")
    public ResponseEntity<StaffingRequest> updateStatus(@PathVariable Long id,
                                                        @RequestParam RosterStatus status) {
        return ResponseEntity.ok(staffingRequestService.updateStatus(id, status));
    }

    // 6. get filtered requests
    @PreAuthorize("!hasAnyRole('Crew')")
    @GetMapping("/")
    public Page<StaffingRequestsSummaryDto> getRequestsFiltered(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<Long> managerId,
            @RequestParam Optional<Long> locationId,
            @RequestParam Optional<String> status
    ) {
        Pageable pageable = PageRequest.of(
                page.orElse(0),
                size.orElse(20),
                Sort.by("createdAt").descending()
        );

        boolean hasFilters =  managerId.isPresent() || locationId.isPresent() || status.isPresent();

        Page<StaffingRequest> result = hasFilters
                ? staffingRequestService.getFilteredRequests( managerId, locationId,status, pageable)
                : staffingRequestService.getRequestsPaged(page.orElse(0), size.orElse(20));

        return result.map(staffingRequestMapper::toSummaryDto);
    }

}
