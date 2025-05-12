package com.airport.admin.airport_admin.controllers;


import com.airport.admin.airport_admin.dto.LeaveRequestDto;
import com.airport.admin.airport_admin.enums.LeaveStatus;
import com.airport.admin.airport_admin.models.LeaveRequest;
import com.airport.admin.airport_admin.security.CustomUserDetails;
import com.airport.admin.airport_admin.services.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaves")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveService;

    // ✅ User applies for leave
    @PostMapping("/apply")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LeaveRequest> applyLeave(@RequestBody LeaveRequestDto dto,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        LeaveRequest leave = leaveService.applyLeave(userDetails.getId(), dto);
        return ResponseEntity.ok(leave);
    }

    // ✅ User views their own leave requests
    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<LeaveRequest>> getMyLeaves(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<LeaveRequest> leaves = leaveService.getLeavesByUser(userDetails.getId());
        return ResponseEntity.ok(leaves);
    }

    // ✅ Admin views all leave requests
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LeaveRequest>> getAllLeaves() {
        return ResponseEntity.ok(leaveService.getAllLeaves());
    }

    // ✅ Admin approves or rejects a leave
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LeaveRequest> updateLeaveStatus(@PathVariable Long id,
                                                          @RequestParam LeaveStatus status) {
        return ResponseEntity.ok(leaveService.updateLeaveStatus(id, status));
    }

    // ✅ User cancels a leave
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LeaveRequest> cancelLeave(@PathVariable Long id,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(leaveService.cancelLeave(id, userDetails.getId()));
    }

    // ✅ User resubmits a previously rejected/cancelled leave
    @PutMapping("/{id}/resubmit")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LeaveRequest> resubmitLeave(@PathVariable Long id,
                                                      @RequestBody LeaveRequestDto dto,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(leaveService.resubmitLeave(id, dto, userDetails.getId()));
    }
}
