package com.airport.admin.airport_admin.features.leave;

import com.airport.admin.airport_admin.enums.LeaveStatus;
import com.airport.admin.airport_admin.features.leave.dto.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    // User: Apply for leave
    @PostMapping("/apply")
    public ResponseEntity<LeaveRequestGetDto> applyLeave(@Valid @RequestBody LeaveRequestCreateDto dto) {
        return ResponseEntity.ok(leaveRequestService.applyLeave(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveRequestGetDto> getLeaveById(@PathVariable Long id){
        return ResponseEntity.ok(leaveRequestService.getLeaveByLeaveId(id));
    }


    // User: View own leave requests
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LeaveRequestGetDto>> getLeavesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(leaveRequestService.getLeavesByUser(userId));
    }

    // Admin/Supervisor/Manager: Paginated + Filtered
    @GetMapping("/")
    @PreAuthorize("hasAnyRole('Admin', 'Supervisor', 'Manager')")
    public ResponseEntity<Page<LeaveRequestGetDto>> getFilteredLeaves(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) LeaveStatus status,
            Pageable pageable
    ) {
        return ResponseEntity.ok(leaveRequestService.getFilteredLeaves(userId, status, pageable));
    }

    // Admin/Supervisor: Update leave status
    @PutMapping("/{id}/status")
    @PreAuthorize("!hasAnyRole('Crew')")
    public ResponseEntity<LeaveRequestGetDto> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody LeaveRequestUpdateDto dto
    ) {
        return ResponseEntity.ok(leaveRequestService.updateLeaveStatus(id, dto.getStatus()));
    }

    // User: Cancel leave
    @PutMapping("/{id}/cancel")
    public ResponseEntity<LeaveRequestGetDto> cancelLeave(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(leaveRequestService.cancelLeave(id, userId));
    }

    // User: Resubmit rejected/cancelled leave
    @PutMapping("/{id}/resubmit")
    public ResponseEntity<LeaveRequestGetDto> resubmitLeave(
            @PathVariable Long id,
            @Valid @RequestBody LeaveRequestCreateDto dto,
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(leaveRequestService.resubmitLeave(id, dto, userId));
    }
}
