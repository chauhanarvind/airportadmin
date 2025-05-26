package com.airport.admin.airport_admin.services;

import com.airport.admin.airport_admin.enums.LeaveStatus;
import com.airport.admin.airport_admin.models.ConstraintProfile;
import com.airport.admin.airport_admin.models.RosterAssignment;
import com.airport.admin.airport_admin.models.StaffAvailability;
import com.airport.admin.airport_admin.repositories.LeaveRequestRepository;
import com.airport.admin.airport_admin.repositories.RosterAssignmentRepository;
import com.airport.admin.airport_admin.models.User;
import com.airport.admin.airport_admin.repositories.StaffAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class RosterServiceHelpers {
    @Autowired RosterAssignmentRepository rosterAssignmentRepository;
    @Autowired StaffAvailabilityRepository staffAvailabilityRepository;
    @Autowired LeaveRequestRepository leaveRequestRepository;

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

            LocalDate currentDate = date;

            boolean isOnLeave = leaveRequestRepository
                    .findByUserIdAndStatus(user.getId(), LeaveStatus.APPROVED)
                    .stream()
                    .anyMatch(leave ->
                            !currentDate.isBefore(leave.getStartDate()) &&
                                    !currentDate.isAfter(leave.getEndDate())
                    );

            if (isOnLeave) {
                continue; // user is fully unavailable that day
            }


            LocalTime dayStart = defaultStart;
            LocalTime dayEnd = defaultEnd;

            List<StaffAvailability> availability = staffAvailabilityRepository.findByUserAndDate(user, date);

            boolean fullyUnavailable = false;

            for (StaffAvailability entry : availability) {

                if (!entry.getIsAvailable()) {
                    if (entry.getUnavailableFrom() == null || entry.getUnavailableTo() == null) {
                        fullyUnavailable = true;
                        break;
                    }

                    // Subtract unavailable block
                    long total = Duration.between(dayStart, dayEnd).toMinutes();
                    long block = Duration.between(entry.getUnavailableFrom(), entry.getUnavailableTo()).toMinutes();
                    totalMinutes += Math.max(total - block, 0);
                    continue;
                }
            }

            if (!fullyUnavailable && availability.isEmpty()) {
                long dayMinutes = Duration.between(dayStart, dayEnd).toMinutes();
                totalMinutes += dayMinutes;
            }
        }

        return totalMinutes / 60;
    }

    public double getFairnessScore(User user, LocalDate fromDate, LocalDate toDate) {
        long worked = getTotalWorkedHours(user, fromDate, toDate);
        long available = getTotalAvailableHours(user, fromDate, toDate);

        if (available == 0) {
            return 1.0; // Fully utilized (or not available at all)
        }

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
        List<StaffAvailability> availabilityList = staffAvailabilityRepository.findByUserAndDate(user, date);
        for (StaffAvailability entry : availabilityList) {
            if (!entry.getIsAvailable()) {
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
