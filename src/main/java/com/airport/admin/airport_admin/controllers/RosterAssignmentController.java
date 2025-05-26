package com.airport.admin.airport_admin.controllers;

import com.airport.admin.airport_admin.dto.RosterAssignmentDto;
import com.airport.admin.airport_admin.mappers.RosterAssignmentMapper;
import com.airport.admin.airport_admin.repositories.RosterAssignmentRepository;
import com.airport.admin.airport_admin.services.RosterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.airport.admin.airport_admin.models.RosterAssignment;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/roster")
public class RosterAssignmentController {

    private final RosterService rosterService;
    private final RosterAssignmentRepository rosterAssignmentRepository;
    @Autowired RosterAssignmentMapper rosterAssignmentMapper;

    public RosterAssignmentController(RosterService rosterService, RosterAssignmentRepository rosterAssignmentRepository) {
        this.rosterService = rosterService;
        this.rosterAssignmentRepository = rosterAssignmentRepository;
    }

    //  Check & Generate
    @PostMapping("/generate/{requestId}")
    public ResponseEntity<?> generateRoster(@PathVariable Long requestId) {
        if (rosterService.rosterExistsForRequest(requestId)) {
            return ResponseEntity.status(409).body("Roster already exists for one or more days. Do you want to regenerate?");
        }

        rosterService.generateRoster(requestId);
        return ResponseEntity.ok("Roster generated successfully.");
    }

    //  Regenerate (delete + generate)
    @PostMapping("/regenerate/{requestId}")
    public ResponseEntity<?> regenerateRoster(@PathVariable Long requestId) {
        rosterService.deleteExistingAssignmentsForRequest(requestId);
        rosterService.generateRoster(requestId);
        return ResponseEntity.ok("Roster regenerated successfully.");
    }

    @GetMapping
    public ResponseEntity<List<RosterAssignmentDto>> getRosterForWeek(
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("locationId") Long locationId
    ) {
        LocalDate endDate = startDate.plusDays(6);
        List<RosterAssignment> assignments = rosterAssignmentRepository
                .findByDateBetweenAndLocationIdOrderByDateAscStartTimeAsc(startDate, endDate, locationId);


        List<RosterAssignmentDto> dtoList = rosterAssignmentMapper.mapToDtoList(assignments);
        return ResponseEntity.ok(dtoList);
    }


}
