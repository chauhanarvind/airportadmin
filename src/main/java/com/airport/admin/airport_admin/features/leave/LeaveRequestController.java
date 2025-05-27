package com.airport.admin.airport_admin.features.leave;

import com.airport.admin.airport_admin.enums.LeaveStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveRequestController {

    private final LeaveRequestService leaveService;

    public LeaveRequestController(LeaveRequestService leaveService) {
        this.leaveService = leaveService;
    }

    // Submit a new leave request
    @PostMapping("/submit/{userId}")
    public ResponseEntity<LeaveRequestDto> applyLeave(
            @PathVariable Long userId,
            @RequestBody LeaveRequestDto dto
    ) {
        LeaveRequestDto leave = leaveService.applyLeave(userId, dto);
        return ResponseEntity.ok(leave);
    }

    // Get leaves for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LeaveRequestDto>> getUserLeaves(@PathVariable Long userId) {
        return ResponseEntity.ok(leaveService.getLeavesByUser(userId));
    }

    // Admin: Get all leaves (paginated & sorted)
    @GetMapping("/all")
    public ResponseEntity<Page<LeaveRequestDto>> getAllLeavesPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(leaveService.getAllLeavesPaged(pageable));
    }

    // Admin: Filtered search (by userId and/or status)
    @GetMapping("/search")
    public ResponseEntity<Page<LeaveRequestDto>> getFilteredLeaves(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) LeaveStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(leaveService.getFilteredLeaves(userId, status, pageable));
    }

    // Admin: Approve/Reject leave
    @PutMapping("/{leaveId}/status")
    public ResponseEntity<LeaveRequestDto> updateStatus(
            @PathVariable Long leaveId,
            @RequestParam LeaveStatus status
    ) {
        return ResponseEntity.ok(leaveService.updateLeaveStatus(leaveId, status));
    }

    //  User: Cancel leave
    @PutMapping("/{leaveId}/cancel/{userId}")
    public ResponseEntity<LeaveRequestDto> cancelLeave(
            @PathVariable Long leaveId,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(leaveService.cancelLeave(leaveId, userId));
    }

    // User: Resubmit rejected/cancelled leave
    @PutMapping("/{leaveId}/resubmit/{userId}")
    public ResponseEntity<LeaveRequestDto> resubmitLeave(
            @PathVariable Long leaveId,
            @PathVariable Long userId,
            @RequestBody LeaveRequestDto dto
    ) {
        return ResponseEntity.ok(leaveService.resubmitLeave(leaveId, dto, userId));
    }
}
