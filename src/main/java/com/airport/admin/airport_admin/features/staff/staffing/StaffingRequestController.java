package com.airport.admin.airport_admin.features.staff.staffing;

import com.airport.admin.airport_admin.enums.RosterStatus;
import com.airport.admin.airport_admin.features.staff.staffing.dto.StaffingRequest.StaffingRequestCreateDto;
import com.airport.admin.airport_admin.features.staff.staffing.dto.StaffingRequest.StaffingRequestDetailDto;
import com.airport.admin.airport_admin.features.staff.staffing.dto.StaffingRequest.StaffingRequestResponseDto;
import com.airport.admin.airport_admin.features.staff.staffing.dto.StaffingRequest.StaffingRequestUpdateDto;
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
@PreAuthorize("!hasAnyRole('Crew')") // can be accessed by anyone except crew
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

    // 3. Approve/reject a request (Admin only)
    @PutMapping("/status/{id}")
    @PreAuthorize("hasRole('Admin')") //can be updated only by admin
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

    // the manager id can be admin or supervisor too.
    @GetMapping("/user/{managerId}")
    @PreAuthorize("#managerId == authentication.principal.id or hasRole('Admin')") // can be accessed only if the data belongs to that user or if the user is admin
    public ResponseEntity<List<StaffingRequestResponseDto>> getByManagerId(@PathVariable Long managerId) {
        List<StaffingRequestResponseDto> requests = staffingRequestService.getRequestsByManagerId(managerId);
        return ResponseEntity.ok(requests);
    }

}
