package com.airport.admin.airport_admin.features.roster;

import com.airport.admin.airport_admin.features.roster.service.RosterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    // ✅ Generate roster
    @PostMapping("/generate/{requestId}")
    public ResponseEntity<?> generateRoster(@PathVariable Long requestId) {
        if (rosterService.rosterExistsForRequest(requestId)) {
            return ResponseEntity.status(409)
                    .body("Roster already exists for this request. Do you want to regenerate?");
        }

        rosterService.generateRoster(requestId);
        return ResponseEntity.ok("Roster generated successfully.");
    }

    // ✅ Check if roster exists
    @GetMapping("/check/{requestId}")
    public ResponseEntity<Boolean> checkIfRosterExists(@PathVariable Long requestId) {
        boolean exists = rosterService.rosterExistsForRequest(requestId);
        return ResponseEntity.ok(exists);
    }

    // ✅ View roster
    @GetMapping("/view/{requestId}")
    public ResponseEntity<List<RosterAssignmentDto>> getRosterForRequest(@PathVariable Long requestId) {
        List<RosterAssignment> assignments = rosterService.getRosterAssignmentsForRequest(requestId);
        List<RosterAssignmentDto> dtoList = rosterAssignmentMapper.mapToDtoList(assignments);
        return ResponseEntity.ok(dtoList);
    }

    // ✅ Regenerate (delete + generate)
    @PostMapping("/regenerate/{requestId}")
    public ResponseEntity<?> regenerateRoster(@PathVariable Long requestId) {
        rosterService.deleteExistingAssignmentsForRequest(requestId);
        rosterService.generateRoster(requestId);
        return ResponseEntity.ok("Roster regenerated successfully.");
    }

    // ✅ Weekly view (filtered by location and date)
    @GetMapping
    public ResponseEntity<?> getRosterForWeek(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("locationId") Long locationId
    ) {
        if (startDate == null || locationId == null) {
            return ResponseEntity.badRequest().body("startDate and locationId are required.");
        }

        LocalDate endDate = startDate.plusDays(6);
        List<RosterAssignment> assignments = rosterAssignmentRepository
                .findByDateBetweenAndLocationIdOrderByDateAscStartTimeAsc(startDate, endDate, locationId);

        List<RosterAssignmentDto> dtoList = rosterAssignmentMapper.mapToDtoList(assignments);
        return ResponseEntity.ok(dtoList);
    }




    @GetMapping("/my/{userId}")
    public ResponseEntity<?> getRosterForUser(@PathVariable Long userId) {
        System.out.println("user id===" + userId);
        List<RosterAssignment> assignments = rosterAssignmentRepository
                .findByUserIdOrderByDateAscStartTimeAsc(userId);

        List<RosterAssignmentDto> dtoList = rosterAssignmentMapper.mapToDtoList(assignments);
        return ResponseEntity.ok(dtoList);
    }

}
