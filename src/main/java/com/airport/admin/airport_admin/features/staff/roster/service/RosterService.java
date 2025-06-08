package com.airport.admin.airport_admin.features.staff.roster.service;

import com.airport.admin.airport_admin.features.Admin.constraintProfile.ConstraintProfileRepository;
import com.airport.admin.airport_admin.features.Admin.jobLevel.JobLevel;
import com.airport.admin.airport_admin.features.Admin.jobRole.JobRole;
import com.airport.admin.airport_admin.features.staff.leave.LeaveRequestRepository;
import com.airport.admin.airport_admin.features.staff.roster.RosterAssignment;
import com.airport.admin.airport_admin.features.staff.roster.RosterAssignmentRepository;
import com.airport.admin.airport_admin.features.staff.staffAvailability.StaffAvailabilityRepository;
import com.airport.admin.airport_admin.features.staff.staffing.model.StaffingRequest;
import com.airport.admin.airport_admin.features.staff.staffing.model.StaffingRequestDay;
import com.airport.admin.airport_admin.features.staff.staffing.model.StaffingRequestItem;
import com.airport.admin.airport_admin.features.staff.staffing.repository.StaffingRequestRepository;
import com.airport.admin.airport_admin.features.Admin.user.User;
import com.airport.admin.airport_admin.features.Admin.user.UserRepository;
import com.airport.admin.airport_admin.utils.InvalidShiftException;
import com.airport.admin.airport_admin.utils.ResourceNotFoundException;
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
    }

    @Transactional
    public void generateRoster(Long staffingRequestId) {
        StaffingRequest request = staffingRequestRepository.findById(staffingRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Staffing request not found"));

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

                List<User> eligibleUsers = userRepository.findAll().stream()
                        .filter(user -> user.getJobRole().getId().equals(requiredRole.getId()))
                        .filter(user -> user.getJobLevel().getId().equals(requiredLevel.getId()))
                        .filter(user -> eligibilityChecker.isEligible(user, date, shiftStart, shiftEnd))
                        .sorted(Comparator.comparingDouble(user ->
                                rosterServiceHelpers.getFairnessScore(user, fairnessFrom, fairnessTo)))
                        .limit(requiredCount)
                        .collect(Collectors.toList());

                for (User selected : eligibleUsers) {
                    RosterAssignment assignment = new RosterAssignment();
                    assignment.setRequest(request);
                    assignment.setUser(selected);
                    assignment.setUnassigned(false);
                    assignment.setDate(date);
                    assignment.setStartTime(shiftStart);
                    assignment.setEndTime(shiftEnd);
                    assignment.setRole(requiredRole);
                    assignment.setLocation(request.getLocation());
                    rosterAssignmentRepository.save(assignment);
                }

                int missingCount = requiredCount - eligibleUsers.size();
                for (int i = 0; i < missingCount; i++) {
                    RosterAssignment unassigned = new RosterAssignment();
                    unassigned.setRequest(request);
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

    public List<RosterAssignment> getRosterAssignmentsForRequest(Long requestId) {
        return rosterAssignmentRepository.findByRequestIdOrderByDateAscStartTimeAsc(requestId);
    }

    public boolean rosterExistsForRequest(Long requestId) {
        return !rosterAssignmentRepository.findByRequestIdOrderByDateAscStartTimeAsc(requestId).isEmpty();
    }

    public void deleteExistingAssignmentsForRequest(Long requestId) {
        rosterAssignmentRepository.deleteByRequestId(requestId);
    }

    public void validateShift(LocalTime start, LocalTime end) {
        if (start == null || end == null || !start.isBefore(end)) {
            throw new InvalidShiftException("Invalid shift timings");
        }
    }

    public boolean isRequestOwnedByUser(Long requestId, Long userId) {
        return staffingRequestRepository.findById(requestId)
                .map(req -> req.getManager().getId().equals(userId))
                .orElse(false);
    }

}
