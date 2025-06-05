package com.airport.admin.airport_admin.features.staff.roster;

import com.airport.admin.airport_admin.features.staff.roster.service.RosterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roster")
public class RosterAssignmentController {

    private final RosterService rosterService;
    private final RosterAssignmentRepository rosterAssignmentRepository;

    @Autowired
    private RosterAssignmentMapper rosterAssignmentMapper;

    public RosterAssignmentController(
            RosterService rosterService,
            RosterAssignmentRepository rosterAssignmentRepository
    ) {
        this.rosterService = rosterService;
        this.rosterAssignmentRepository = rosterAssignmentRepository;
    }

    // Admin: Generate roster for a request
    @PostMapping("/generate/{requestId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?> generateRoster(@PathVariable Long requestId) {
        if (rosterService.rosterExistsForRequest(requestId)) {
            return ResponseEntity.status(409)
                    .body("Roster already exists for this request. Do you want to regenerate?");
        }
        rosterService.generateRoster(requestId);
        return ResponseEntity.ok("Roster generated successfully.");
    }

    // Admin: Check if roster already exists for a request...this is used during generate roster
    @GetMapping("/check/{requestId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Boolean> checkIfRosterExists(@PathVariable Long requestId) {
        return ResponseEntity.ok(rosterService.rosterExistsForRequest(requestId));
    }

    // Admin: View roster assignments for a request (shows the detail)
    @GetMapping("/view/{requestId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<RosterAssignmentDto>> getRosterForRequest(@PathVariable Long requestId) {
        var assignments = rosterService.getRosterAssignmentsForRequest(requestId);
        var dtoList = rosterAssignmentMapper.mapToDtoList(assignments);
        return ResponseEntity.ok(dtoList);
    }

    // Admin: Regenerate the roster (delete existing and re-generate)
    @PostMapping("/regenerate/{requestId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> regenerateRoster(@PathVariable Long requestId) {
        rosterService.deleteExistingAssignmentsForRequest(requestId);
        rosterService.generateRoster(requestId);
        return ResponseEntity.ok("Roster regenerated successfully.");
    }

    // User/Admin: View personal roster
    @GetMapping("/my/{userId}")
    @PreAuthorize("#userId == authentication.principal.id or hasRole('Admin')")
    public ResponseEntity<List<RosterAssignmentDto>> getRosterForUser(@PathVariable Long userId) {
        var assignments = rosterAssignmentRepository.findByUserIdOrderByDateAscStartTimeAsc(userId);
        var dtoList = rosterAssignmentMapper.mapToDtoList(assignments);
        return ResponseEntity.ok(dtoList);
    }
}
