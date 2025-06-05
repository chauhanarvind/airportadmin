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
public class UserEligibilityChecker {

    private static final int DEFAULT_MAX_HOURS_PER_DAY = 8;
    private static final int DEFAULT_MAX_HOURS_PER_WEEK = 40;

    @Autowired private LeaveRequestRepository leaveRequestRepository;
    @Autowired private StaffAvailabilityRepository staffAvailabilityRepository;
    @Autowired private RosterAssignmentRepository rosterAssignmentRepository;

    /**
     * Master method to check full eligibility for a shift.
     */
    public boolean isEligible(User user, LocalDate date, LocalTime shiftStart, LocalTime shiftEnd) {
        if (!isValidShift(shiftStart, shiftEnd)) return false;

        ConstraintProfile profile = user.getConstraintProfile();

        return isAllowedDay(profile, date) &&
                isWithinPreferredTime(profile, shiftStart, shiftEnd) &&
                !isUserOnLeave(user, date) &&
                !hasUnavailabilityConflict(user, date, shiftStart, shiftEnd) &&
                !hasRosterConflict(user, date, shiftStart, shiftEnd) &&
                !exceedsDailyLimit(user, date, shiftStart, shiftEnd, profile) &&
                !exceedsWeeklyLimit(user, date, shiftStart, shiftEnd, profile);
    }

    // === Constraint profile checks ===

    private boolean isAllowedDay(ConstraintProfile profile, LocalDate date) {
        if (profile == null || profile.getAllowedDays() == null) return true;
        return profile.getAllowedDays().contains(date.getDayOfWeek());
    }

    private boolean isWithinPreferredTime(ConstraintProfile profile, LocalTime shiftStart, LocalTime shiftEnd) {
        if (profile == null || profile.getPreferredStartTime() == null || profile.getPreferredEndTime() == null) {
            return true;
        }
        return !shiftStart.isBefore(profile.getPreferredStartTime()) &&
                !shiftEnd.isAfter(profile.getPreferredEndTime());
    }

    // === Leave and availability ===

    private boolean isUserOnLeave(User user, LocalDate date) {
        return leaveRequestRepository.existsByUserIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                user.getId(), LeaveStatus.APPROVED, date, date);
    }

    private boolean hasUnavailabilityConflict(User user, LocalDate date, LocalTime shiftStart, LocalTime shiftEnd) {
        Optional<StaffAvailability> availabilityOpt = staffAvailabilityRepository.findByUserIdAndDate(user.getId(), date);
        if (availabilityOpt.isEmpty()) return false;

        StaffAvailability availability = availabilityOpt.get();
        if (availability.isAvailable()) return false;

        LocalTime from = availability.getUnavailableFrom();
        LocalTime to = availability.getUnavailableTo();

        // Fully unavailable
        if (!isValidShift(from, to)) return true;

        // Overlap check
        return !shiftEnd.isBefore(from) && !shiftStart.isAfter(to);
    }

    // === Shift conflict and hour limits ===

    private boolean hasRosterConflict(User user, LocalDate date, LocalTime shiftStart, LocalTime shiftEnd) {
        List<RosterAssignment> assignments = rosterAssignmentRepository.findByUserIdAndDate(user.getId(), date);
        return assignments.stream()
                .anyMatch(ra ->
                        isValidShift(ra.getStartTime(), ra.getEndTime()) &&
                                !shiftEnd.isBefore(ra.getStartTime()) &&
                                !shiftStart.isAfter(ra.getEndTime())
                );
    }

    private boolean exceedsDailyLimit(User user, LocalDate date, LocalTime shiftStart, LocalTime shiftEnd, ConstraintProfile profile) {
        int maxDaily = (profile != null && profile.getMaxHoursPerDay() != null)
                ? profile.getMaxHoursPerDay()
                : DEFAULT_MAX_HOURS_PER_DAY;

        long newShiftMinutes = Duration.between(shiftStart, shiftEnd).toMinutes();

        long existingMinutes = rosterAssignmentRepository.findByUserIdAndDate(user.getId(), date).stream()
                .filter(a -> isValidShift(a.getStartTime(), a.getEndTime()))
                .mapToLong(a -> Duration.between(a.getStartTime(), a.getEndTime()).toMinutes())
                .sum();

        return (existingMinutes + newShiftMinutes) > (maxDaily * 60);
    }

    private boolean exceedsWeeklyLimit(User user, LocalDate date, LocalTime shiftStart, LocalTime shiftEnd, ConstraintProfile profile) {
        int maxWeekly = (profile != null && profile.getMaxHoursPerWeek() != null)
                ? profile.getMaxHoursPerWeek()
                : DEFAULT_MAX_HOURS_PER_WEEK;

        long newShiftMinutes = Duration.between(shiftStart, shiftEnd).toMinutes();

        LocalDate startOfWeek = date.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = date.with(DayOfWeek.SUNDAY);

        long existingMinutes = rosterAssignmentRepository.findByUserIdAndDateBetween(user.getId(), startOfWeek, endOfWeek).stream()
                .filter(a -> isValidShift(a.getStartTime(), a.getEndTime()))
                .mapToLong(a -> Duration.between(a.getStartTime(), a.getEndTime()).toMinutes())
                .sum();

        return (existingMinutes + newShiftMinutes) > (maxWeekly * 60);
    }

    private boolean isValidShift(LocalTime start, LocalTime end) {
        return start != null && end != null && start.isBefore(end);
    }
}
