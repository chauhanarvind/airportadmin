package com.airport.admin.airport_admin.services;

import com.airport.admin.airport_admin.models.*;
import com.airport.admin.airport_admin.repositories.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RosterService {
    private final UserRepository userRepository;
    private final RosterAssignmentRepository rosterAssignmentRepository;

    public RosterService(UserRepository userRepository,
                         RosterAssignmentRepository rosterAssignmentRepository) {
        this.userRepository = userRepository;
        this.rosterAssignmentRepository = rosterAssignmentRepository;
    }

    public List<RosterAssignment> generateRosterFromRequest(StaffingRequest request) {
        List<RosterAssignment> assignments = new ArrayList<>();

        for (StaffingRequestDay day : request.getDays()) {
            String requestDate = day.getDate();

            for (StaffingRequestItem item : day.getItems()) {
                int requiredCount = item.getRequiredCount();
                JobRole role = item.getRole();
                JobLevel level = item.getJobLevel();
                String start = item.getStartTime();
                String end = item.getEndTime();

                // Step 1: Find available staff
                List<User> availableStaff = userRepository
                        .findByJobRoleAndJobLevel(role, level)
                        .stream()
                        .filter(s -> isStaffAvailable(s, requestDate, start, end))
                        .limit(requiredCount)
                        .collect(Collectors.toList());

                // Step 2: Create RosterAssignment entries
                for (Staff staff : availableStaff) {
                    RosterAssignment assignment = new RosterAssignment();
                    assignment.setStaff(staff);
                    assignment.setDate(requestDate);
                    assignment.setStartTime(start);
                    assignment.setEndTime(end);
                    assignment.setRole(role);
                    assignment.setLocation(request.getLocation());

                    assignments.add(assignment);
                }

                // Optional: Warn if not enough staff
                if (availableStaff.size() < requiredCount) {
                    System.out.println("Warning: Only " + availableStaff.size() +
                            " staff assigned for " + role.getName() + " on " + requestDate +
                            ", required: " + requiredCount);
                }
            }
        }

        // Step 3: Save to DB
        return rosterAssignmentRepository.saveAll(assignments);
    }

    private boolean isStaffAvailable(Staff staff, LocalDate date, LocalTime start, LocalTime end) {

        List<RosterAssignment> existingAssignments = rosterAssignmentRepository
                .findByStaffAndDate(staff, date);

        for (RosterAssignment assignment : existingAssignments) {
            if (start.isBefore(assignment.getEndTime()) && end.isAfter(assignment.getStartTime())) {
                return false; // Conflict
            }
        }

        return true;
    }
}
