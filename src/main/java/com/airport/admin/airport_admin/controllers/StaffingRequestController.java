package com.airport.admin.airport_admin.controllers;

import com.airport.admin.airport_admin.dto.StaffingRequestsDto;
import com.airport.admin.airport_admin.enums.LeaveStatus;
import com.airport.admin.airport_admin.models.StaffingRequest;
import com.airport.admin.airport_admin.services.StaffingRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staffing-requests")
public class StaffingRequestController {

    @Autowired
    private StaffingRequestService staffingRequestService;

    // 1. Submit a new staffing request
    @PostMapping("/submit")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<StaffingRequest> submitRequest(@RequestBody StaffingRequestsDto dto) {
        StaffingRequest request = staffingRequestService.saveRequest(dto);
        return ResponseEntity.ok(request);
    }

    // 2. Get all requests (admin only)
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StaffingRequest>> getAllRequests() {
        return ResponseEntity.ok(staffingRequestService.getAllRequests());
    }

    // 3. Get requests by manager ID (e.g. manager dashboard)
    @GetMapping("/manager/{managerId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<StaffingRequest>> getRequestsByManager(@PathVariable Long managerId) {
        return ResponseEntity.ok(staffingRequestService.getRequestsByManager(managerId));
    }

    // 4. Get a specific request by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<StaffingRequest> getById(@PathVariable Long id) {
        return ResponseEntity.ok(staffingRequestService.getRequestById(id));
    }

    // 5. Approve/reject a request (admin only)
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StaffingRequest> updateStatus(@PathVariable Long id,
                                                        @RequestParam LeaveStatus status) {
        return ResponseEntity.ok(staffingRequestService.updateStatus(id, status));
    }
}
