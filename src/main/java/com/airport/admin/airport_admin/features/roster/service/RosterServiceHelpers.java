package com.airport.admin.airport_admin.features.roster.service;

import com.airport.admin.airport_admin.enums.LeaveStatus;
import com.airport.admin.airport_admin.features.constraintProfile.ConstraintProfile;
import com.airport.admin.airport_admin.features.roster.RosterAssignment;
import com.airport.admin.airport_admin.features.roster.RosterAssignmentRepository;
import com.airport.admin.airport_admin.features.staffAvailability.StaffAvailability;
import com.airport.admin.airport_admin.features.leave.LeaveRequestRepository;
import com.airport.admin.airport_admin.features.user.User;
import com.airport.admin.airport_admin.features.staffAvailability.StaffAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Component
public class RosterServiceHelpers {
    @Autowired
    RosterAssignmentRepository rosterAssignmentRepository;

    @Autowired
    StaffAvailabilityRepository staffAvailabilityRepository;

    @Autowired
    LeaveRequestRepository leaveRequestRepository;

    public long getTotalWorkedHours(User user, LocalDate fromDate, LocalDate toDate) {
        List<RosterAssignment> assignments = rosterAssignmentRepository
                .findByUserIdAndDateBetween(user.getId(), fromDate, toDate);

        long totalMinutes = 0;

        for (RosterAssignment ra : assignments) {
            long minutes = Duration.between(ra.getStartTime(), ra.getEndTime()).toMinutes();
            totalMinutes += Math.max(minutes, 0); // in case of invalid data
        }

        return totalMinutes / 60; // convert to hours
    }

    public long getTotalAvailableHours(User user, LocalDate fromDate, LocalDate toDate) {
        long totalMinutes = 0;

        ConstraintProfile profile = user.getConstraintProfile();
        List<DayOfWeek> allowedDays = (profile != null && profile.getAllowedDays() != null)
                ? profile.getAllowedDays()
                : List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY); // default

        LocalTime defaultStart = (profile != null && profile.getPreferredStartTime() != null)
                ? profile.getPreferredStartTime()
                : LocalTime.of(9, 0);

        LocalTime defaultEnd = (profile != null && profile.getPreferredEndTime() != null)
                ? profile.getPreferredEndTime()
                : LocalTime.of(17, 0);

        for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            if (!allowedDays.contains(date.getDayOfWeek())) continue;
            final LocalDate currentDate = date;

            boolean isOnLeave = leaveRequestRepository
                    .findByUserIdAndStatus(user.getId(), LeaveStatus.APPROVED)
                    .stream()
                    .anyMatch(leave -> !currentDate.isBefore(leave.getStartDate()) && !currentDate.isAfter(leave.getEndDate()));

            if (isOnLeave) continue;



            Optional<StaffAvailability> availabilityOpt = staffAvailabilityRepository.findByUserAndDate(user, date);

            if (availabilityOpt.isPresent()) {
                StaffAvailability entry = availabilityOpt.get();

                if (!entry.isAvailable()) {
                    if (entry.getUnavailableFrom() == null || entry.getUnavailableTo() == null) {
                        continue; // fully unavailable, skip this day
                    }

                    long total = Duration.between(defaultStart, defaultEnd).toMinutes();
                    long block = Duration.between(entry.getUnavailableFrom(), entry.getUnavailableTo()).toMinutes();
                    totalMinutes += Math.max(total - block, 0);
                    continue;
                }
            } else {
                // No availability set — assume default available
                long dayMinutes = Duration.between(defaultStart, defaultEnd).toMinutes();
                totalMinutes += dayMinutes;
            }
        }

        return totalMinutes / 60;
    }

    public double getFairnessScore(User user, LocalDate fromDate, LocalDate toDate) {
        long worked = getTotalWorkedHours(user, fromDate, toDate);
        long available = getTotalAvailableHours(user, fromDate, toDate);

        if (available == 0) return 1.0;

        return (double) worked / available;
    }

    public boolean isUserAvailable(User user, LocalDate date, LocalTime shiftStart, LocalTime shiftEnd) {
        // 1. Check approved leave
        boolean onLeave = leaveRequestRepository
                .findByUserIdAndStatus(user.getId(), LeaveStatus.APPROVED)
                .stream()
                .anyMatch(leave -> !date.isBefore(leave.getStartDate()) && !date.isAfter(leave.getEndDate()));
        if (onLeave) return false;

        // 2. Check partial unavailability
        Optional<StaffAvailability> availabilityOpt = staffAvailabilityRepository.findByUserAndDate(user, date);
        if (availabilityOpt.isPresent()) {
            StaffAvailability entry = availabilityOpt.get();

            if (!entry.isAvailable()) {
                LocalTime unavailableFrom = entry.getUnavailableFrom();
                LocalTime unavailableTo = entry.getUnavailableTo();

                if (unavailableFrom == null || unavailableTo == null) {
                    return false; // full-day unavailable
                }

                boolean overlaps = !shiftEnd.isBefore(unavailableFrom) && !shiftStart.isAfter(unavailableTo);
                if (overlaps) return false;
            }
        }

        // 3. Check existing roster conflicts
        List<RosterAssignment> assignments = rosterAssignmentRepository.findByUserIdAndDate(user.getId(), date);
        for (RosterAssignment ra : assignments) {
            boolean overlaps = !shiftEnd.isBefore(ra.getStartTime()) && !shiftStart.isAfter(ra.getEndTime());
            if (overlaps) return false;
        }

        return true;
    }
}
