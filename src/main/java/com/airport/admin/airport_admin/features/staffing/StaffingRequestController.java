package com.airport.admin.airport_admin.features.staffing;

import com.airport.admin.airport_admin.enums.RosterStatus;
import com.airport.admin.airport_admin.features.staffing.dto.StaffingRequest.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/staffing-requests")
public class StaffingRequestController {

    @Autowired
    private StaffingRequestService staffingRequestService;

    // 1. Submit a new staffing request
    @PostMapping("/submit")
    public ResponseEntity<StaffingRequestResponseDto> submitRequest(
            @Valid @RequestBody StaffingRequestCreateDto dto
    ) {
        StaffingRequestResponseDto response = staffingRequestService.submitRequest(dto);
        return ResponseEntity.ok(response);
    }

    // 2. Get a specific request by ID (detailed view)
    @GetMapping("/{id}")
    public ResponseEntity<StaffingRequestDetailDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(staffingRequestService.getRequestById(id));
    }

    // 3. Approve/reject a request (Admin or Supervisor only)
    @PutMapping("/{id}/status")
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
            @RequestParam Optional<String> status
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

    @GetMapping("/user/{managerId}")
    public ResponseEntity<List<StaffingRequestResponseDto>> getByManagerId(@PathVariable Long managerId) {
        List<StaffingRequestResponseDto> requests = staffingRequestService.getRequestsByManagerId(managerId);
        return ResponseEntity.ok(requests);
    }

}
