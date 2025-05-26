package com.airport.admin.airport_admin.services;

import com.airport.admin.airport_admin.models.*;
import com.airport.admin.airport_admin.repositories.*;
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
    @Autowired RosterServiceHelpers rosterServiceHelpers;

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

        List<StaffingRequestDay> days = request.getDays();

        LocalDate fairnessFrom = LocalDate.now().minusWeeks(2);
        LocalDate fairnessTo = LocalDate.now();

        for (StaffingRequestDay day : days) {
            LocalDate date = day.getDate();

            for (StaffingRequestItem item : day.getItems()) {
                JobRole requiredRole = item.getJobRole();
                JobLevel requiredLevel = item.getJobLevel();
                int requiredCount = item.getRequiredCount();
                LocalTime shiftStart = item.getStartTime();
                LocalTime shiftEnd = item.getEndTime();

                List<User> allUsers = userRepository.findAll();

                List<User> eligibleUsers = allUsers.stream()
                        .filter(user -> user.getJobRole().getId().equals(requiredRole.getId()))
                        .filter(user -> user.getJobLevel().getId().equals(requiredLevel.getId()))
                        .filter(user -> rosterServiceHelpers.isUserAvailable(user, date, shiftStart, shiftEnd))
                        .collect(Collectors.toList());

                eligibleUsers.sort(Comparator.comparingDouble(user ->
                        rosterServiceHelpers.getFairnessScore(user, fairnessFrom, fairnessTo)
                ));


                List<User> selectedUsers = eligibleUsers.stream()
                        .limit(requiredCount)
                        .collect(Collectors.toList());



                for (User selected : selectedUsers) {
                    RosterAssignment assignment = new RosterAssignment();
                    assignment.setUser(selected);
                    assignment.setDate(date);
                    assignment.setStartTime(shiftStart);
                    assignment.setEndTime(shiftEnd);
                    assignment.setRole(requiredRole);
                    assignment.setLocation(request.getLocation());

                    rosterAssignmentRepository.save(assignment);
                }


            }
        }

    }

    public boolean rosterExistsForRequest(Long staffingRequestId) {
        StaffingRequest request = staffingRequestRepository.findById(staffingRequestId)
                .orElseThrow(() -> new RuntimeException("Staffing request not found"));

        List<LocalDate> dates = request.getDays().stream()
                .map(StaffingRequestDay::getDate)
                .toList();

        Long locationId = request.getLocation().getId();

        for (LocalDate date : dates) {
            boolean exists = !rosterAssignmentRepository.findByDateAndLocationId(date, locationId).isEmpty();
            if (exists) return true;
        }

        return false;
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


}
