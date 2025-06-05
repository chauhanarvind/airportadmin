package com.airport.admin.airport_admin.features.staff.leave;

import com.airport.admin.airport_admin.enums.LeaveStatus;
import com.airport.admin.airport_admin.features.staff.leave.dto.LeaveRequestCreateDto;
import com.airport.admin.airport_admin.features.staff.leave.dto.LeaveRequestGetDto;
import com.airport.admin.airport_admin.features.staff.leave.dto.LeaveRequestUpdateDto;
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
    @PreAuthorize("@securityService.isOwner(#dto.userId, authentication)") //only for the owner
    public ResponseEntity<LeaveRequestGetDto> applyLeave(@Valid @RequestBody LeaveRequestCreateDto dto) {
        return ResponseEntity.ok(leaveRequestService.applyLeave(dto));
    }

    // View leave by ID (owner or admin)
    @GetMapping("/{id}")
    @PreAuthorize("@leaveSecurity.canView(#id, authentication) or @securityService.isAdmin(authentication)")
    public ResponseEntity<LeaveRequestGetDto> getLeaveById(@PathVariable Long id) {
        return ResponseEntity.ok(leaveRequestService.getLeaveByLeaveId(id));
    }

    // View all leave requests for a user (owner only)
    @GetMapping("/user/{userId}")
    @PreAuthorize("@securityService.isOwner(#userId, authentication)")
    public ResponseEntity<List<LeaveRequestGetDto>> getLeavesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(leaveRequestService.getLeavesByUser(userId));
    }

    // Admin/Supervisor/Manager: Paginated + filtered
    @GetMapping
    @PreAuthorize("!hasAnyRole('Crew')")
    public ResponseEntity<Page<LeaveRequestGetDto>> getFilteredLeaves(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) LeaveStatus status,
            Pageable pageable
    ) {
        return ResponseEntity.ok(leaveRequestService.getFilteredLeaves(userId, status, pageable));
    }

    // Admin: Update leave status
    @PutMapping("/{id}/status")
    @PreAuthorize("@securityService.isAdmin(authentication)")
    public ResponseEntity<LeaveRequestGetDto> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody LeaveRequestUpdateDto dto
    ) {
        return ResponseEntity.ok(leaveRequestService.updateLeaveStatus(id, dto.getStatus()));
    }

    // User: Cancel own leave
    @PutMapping("/{id}/cancel")
    @PreAuthorize("@leaveSecurity.canView(#id, authentication)")
    public ResponseEntity<LeaveRequestGetDto> cancelLeave(@PathVariable Long id) {
        return ResponseEntity.ok(leaveRequestService.cancelLeave(id));
    }

    // User: Resubmit rejected/cancelled leave
    @PutMapping("/{id}/resubmit")
    @PreAuthorize("@leaveSecurity.canView(#id, authentication)")
    public ResponseEntity<LeaveRequestGetDto> resubmitLeave(
            @PathVariable Long id,
            @Valid @RequestBody LeaveRequestCreateDto dto
    ) {
        return ResponseEntity.ok(leaveRequestService.resubmitLeave(id, dto));
    }
}
