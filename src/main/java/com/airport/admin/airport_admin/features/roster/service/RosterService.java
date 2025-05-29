package com.airport.admin.airport_admin.features.roster.service;

import com.airport.admin.airport_admin.features.constraintProfile.ConstraintProfileRepository;
import com.airport.admin.airport_admin.features.jobLevel.JobLevel;
import com.airport.admin.airport_admin.features.jobRole.JobRole;
import com.airport.admin.airport_admin.features.leave.LeaveRequestRepository;
import com.airport.admin.airport_admin.features.roster.RosterAssignment;
import com.airport.admin.airport_admin.features.roster.RosterAssignmentRepository;
import com.airport.admin.airport_admin.features.roster.UserEligibilityChecker;
import com.airport.admin.airport_admin.features.staffAvailability.StaffAvailabilityRepository;
import com.airport.admin.airport_admin.features.staffing.model.StaffingRequest;
import com.airport.admin.airport_admin.features.staffing.model.StaffingRequestDay;
import com.airport.admin.airport_admin.features.staffing.model.StaffingRequestItem;
import com.airport.admin.airport_admin.features.staffing.repository.StaffingRequestRepository;
import com.airport.admin.airport_admin.features.user.User;
import com.airport.admin.airport_admin.features.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RosterService {

    private final StaffingRequestRepository staffingRequestRepository;
    private final RosterAssignmentRepository rosterAssignmentRepository;
    private final UserRepository userRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final StaffAvailabilityRepository staffAvailabilityRepository;
    private final ConstraintProfileRepository constraintProfileRepository;

    @Autowired private RosterServiceHelpers rosterServiceHelpers;
    @Autowired private UserEligibilityChecker eligibilityChecker;

    public RosterService(
            StaffingRequestRepository staffingRequestRepository,
            RosterAssignmentRepository rosterAssignmentRepository,
            UserRepository userRepository,
            LeaveRequestRepository leaveRequestRepository,
            StaffAvailabilityRepository staffAvailabilityRepository,
            ConstraintProfileRepository constraintProfileRepository
    ) {
        this.staffingRequestRepository = staffingRequestRepository;
        this.rosterAssignmentRepository = rosterAssignmentRepository;
        this.userRepository = userRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.staffAvailabilityRepository = staffAvailabilityRepository;
        this.constraintProfileRepository = constraintProfileRepository;
    }

    @Transactional
    public void generateRoster(Long staffingRequestId) {
        StaffingRequest request = staffingRequestRepository.findById(staffingRequestId)
                .orElseThrow(() -> new RuntimeException("Staffing request not found"));

        int totalUnassignedCount = 0;
        LocalDate fairnessFrom = LocalDate.now().minusWeeks(2);
        LocalDate fairnessTo = LocalDate.now();

        for (StaffingRequestDay day : request.getDays()) {
            LocalDate date = day.getDate();

            for (StaffingRequestItem item : day.getItems()) {
                JobRole requiredRole = item.getJobRole();
                JobLevel requiredLevel = item.getJobLevel();
                int requiredCount = item.getRequiredCount();
                LocalTime shiftStart = item.getStartTime();
                LocalTime shiftEnd = item.getEndTime();

                validateShift(shiftStart, shiftEnd);

                // Find eligible users
                List<User> eligibleUsers = userRepository.findAll().stream()
                        .filter(user -> user.getJobRole().getId().equals(requiredRole.getId()))
                        .filter(user -> user.getJobLevel().getId().equals(requiredLevel.getId()))
                        .filter(user -> eligibilityChecker.isEligible(user, date, shiftStart, shiftEnd))
                        .sorted(Comparator.comparingDouble(user ->
                                rosterServiceHelpers.getFairnessScore(user, fairnessFrom, fairnessTo)))
                        .limit(requiredCount)
                        .collect(Collectors.toList());

                // Assign selected users
                for (User selected : eligibleUsers) {
                    RosterAssignment assignment = new RosterAssignment();
                    assignment.setUser(selected);
                    assignment.setUnassigned(false);
                    assignment.setDate(date);
                    assignment.setStartTime(shiftStart);
                    assignment.setEndTime(shiftEnd);
                    assignment.setRole(requiredRole);
                    assignment.setLocation(request.getLocation());
                    rosterAssignmentRepository.save(assignment);
                }

                // Create placeholders for unassigned slots
                int missingCount = requiredCount - eligibleUsers.size();
                for (int i = 0; i < missingCount; i++) {
                    RosterAssignment unassigned = new RosterAssignment();
                    unassigned.setUser(null);
                    unassigned.setUnassigned(true);
                    unassigned.setDate(date);
                    unassigned.setStartTime(shiftStart);
                    unassigned.setEndTime(shiftEnd);
                    unassigned.setRole(requiredRole);
                    unassigned.setLocation(request.getLocation());
                    rosterAssignmentRepository.save(unassigned);
                }

                totalUnassignedCount += missingCount;
            }
        }

        request.setUnassignedShiftCount(totalUnassignedCount);
        staffingRequestRepository.save(request);
    }

    public boolean rosterExistsForRequest(Long staffingRequestId) {
        StaffingRequest request = staffingRequestRepository.findById(staffingRequestId)
                .orElseThrow(() -> new RuntimeException("Staffing request not found"));

        Long locationId = request.getLocation().getId();

        return request.getDays().stream()
                .map(StaffingRequestDay::getDate)
                .anyMatch(date ->
                        !rosterAssignmentRepository.findByDateAndLocationId(date, locationId).isEmpty());
    }

    public void deleteExistingAssignmentsForRequest(Long staffingRequestId) {
        StaffingRequest request = staffingRequestRepository.findById(staffingRequestId)
                .orElseThrow(() -> new RuntimeException("Staffing request not found"));

        List<LocalDate> dates = request.getDays().stream()
                .map(StaffingRequestDay::getDate)
                .toList();

        Long locationId = request.getLocation().getId();
        rosterAssignmentRepository.deleteByDateInAndLocationId(dates, locationId);
    }

    public void validateShift(LocalTime start, LocalTime end) {
        if (start == null || end == null || !start.isBefore(end)) {
            throw new IllegalArgumentException("Invalid shift timings");
        }
    }
}
