package com.airport.admin.airport_admin.features.roster.service;

import com.airport.admin.airport_admin.enums.LeaveStatus;
import com.airport.admin.airport_admin.features.constraintProfile.ConstraintProfile;
import com.airport.admin.airport_admin.features.leave.LeaveRequestRepository;
import com.airport.admin.airport_admin.features.roster.RosterAssignment;
import com.airport.admin.airport_admin.features.roster.RosterAssignmentRepository;
import com.airport.admin.airport_admin.features.staffAvailability.StaffAvailability;
import com.airport.admin.airport_admin.features.staffAvailability.StaffAvailabilityRepository;
import com.airport.admin.airport_admin.features.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.List;
import java.util.Optional;

@Component
public class RosterServiceHelpers {

    @Autowired private RosterAssignmentRepository rosterAssignmentRepository;
    @Autowired private StaffAvailabilityRepository staffAvailabilityRepository;
    @Autowired private LeaveRequestRepository leaveRequestRepository;

    /**
     * Calculates total hours a user worked between two dates.
     */
    public long getTotalWorkedHours(User user, LocalDate fromDate, LocalDate toDate) {
        List<RosterAssignment> assignments = rosterAssignmentRepository
                .findByUserIdAndDateBetween(user.getId(), fromDate, toDate);

        return assignments.stream()
                .filter(a -> isValidShift(a.getStartTime(), a.getEndTime()))
                .mapToLong(a -> Duration.between(a.getStartTime(), a.getEndTime()).toMinutes())
                .sum() / 60;
    }

    /**
     * Calculates total available hours for a user between two dates, considering profile, leave, and availability.
     */
    public long getTotalAvailableHours(User user, LocalDate fromDate, LocalDate toDate) {
        ConstraintProfile profile = user.getConstraintProfile();

        List<DayOfWeek> allowedDays = (profile != null && profile.getAllowedDays() != null)
                ? profile.getAllowedDays()
                : List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);

        LocalTime defaultStart = (profile != null && profile.getPreferredStartTime() != null)
                ? profile.getPreferredStartTime()
                : LocalTime.of(9, 0);

        LocalTime defaultEnd = (profile != null && profile.getPreferredEndTime() != null)
                ? profile.getPreferredEndTime()
                : LocalTime.of(17, 0);

        if (!defaultStart.isBefore(defaultEnd)) return 0;

        long totalMinutes = 0;

        for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            if (!allowedDays.contains(date.getDayOfWeek())) continue;
            if (isUserOnLeave(user, date)) continue;

            Optional<StaffAvailability> availabilityOpt = staffAvailabilityRepository.findByUserAndDate(user, date);
            if (availabilityOpt.isPresent() && !availabilityOpt.get().isAvailable()) {
                StaffAvailability entry = availabilityOpt.get();
                LocalTime from = entry.getUnavailableFrom();
                LocalTime to = entry.getUnavailableTo();

                if (isValidShift(from, to)) {
                    long total = Duration.between(defaultStart, defaultEnd).toMinutes();
                    long block = Duration.between(from, to).toMinutes();
                    totalMinutes += Math.max(total - block, 0);
                }
                // else: fully unavailable
            } else {
                totalMinutes += Duration.between(defaultStart, defaultEnd).toMinutes();
            }
        }

        return totalMinutes / 60;
    }

    /**
     * Computes fairness score = worked / available hours.
     */
    public double getFairnessScore(User user, LocalDate fromDate, LocalDate toDate) {
        long worked = getTotalWorkedHours(user, fromDate, toDate);
        long available = getTotalAvailableHours(user, fromDate, toDate);
        return (available == 0) ? 1.0 : (double) worked / available;
    }

    /**
     * Util: Checks if a shift is valid.
     */
    private boolean isValidShift(LocalTime start, LocalTime end) {
        return start != null && end != null && start.isBefore(end);
    }

    /**
     * Util: Checks if the user is on approved leave on a given date.
     */
    private boolean isUserOnLeave(User user, LocalDate date) {
        return leaveRequestRepository.findByUserIdAndStatus(user.getId(), LeaveStatus.APPROVED).stream()
                .anyMatch(leave -> !date.isBefore(leave.getStartDate()) && !date.isAfter(leave.getEndDate()));
    }
}
