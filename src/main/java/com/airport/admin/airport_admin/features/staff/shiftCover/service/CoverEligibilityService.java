package com.airport.admin.airport_admin.features.staff.shiftCover.service;

import com.airport.admin.airport_admin.enums.LeaveStatus;
import com.airport.admin.airport_admin.features.Admin.user.User;
import com.airport.admin.airport_admin.features.staff.leave.LeaveRequestRepository;
import com.airport.admin.airport_admin.features.staff.roster.RosterAssignment;
import com.airport.admin.airport_admin.features.staff.roster.RosterAssignmentRepository;
import com.airport.admin.airport_admin.features.staff.staffAvailability.StaffAvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoverEligibilityService {

    private final LeaveRequestRepository leaveRepo;
    private final StaffAvailabilityRepository availabilityRepo;
    private final RosterAssignmentRepository rosterRepo;

    public List<String> checkWarnings(
            User coveringUser,
            User originalUser,
            RosterAssignment shift
    )
    {
        List<String> warnings = new ArrayList<>();

        LocalDate shiftDate = shift.getDate();
        LocalTime startTime = shift.getStartTime();
        LocalTime endTime = shift.getEndTime();

        // 1️ Leave check
        boolean onLeave = leaveRepo.existsByUserAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                coveringUser, LeaveStatus.APPROVED, shiftDate, shiftDate
        );
        if (onLeave) {
            warnings.add("User is on approved leave during this date.");
        }

        // 2️ Availability check
        availabilityRepo.findByUserAndDate(coveringUser, shiftDate).ifPresent(availability -> {
            if (!availability.isAvailable()) {
                warnings.add("User is marked unavailable on this date.");
            } else if (
                    availability.getUnavailableFrom() != null &&
                            availability.getUnavailableTo() != null &&
                            timeOverlap(startTime, endTime, availability.getUnavailableFrom(), availability.getUnavailableTo())
            ) {
                warnings.add("User is partially unavailable during this shift.");
            }
        });

        // 3️ Roster conflict check
        boolean hasConflict = rosterRepo.existsByUserAndShiftDateAndTimeOverlap(
                coveringUser, shiftDate, startTime, endTime
        );
        if (hasConflict) {
            warnings.add("User is already assigned to an overlapping shift.");
        }

        // 4️ Role mismatch
        if (!coveringUser.getRole().getName()
                .equalsIgnoreCase(originalUser.getRole().getName())) {
            warnings.add("User's role does not match the original user's role.");
        }

        // 5️ Job role mismatch
        if (!coveringUser.getJobRole().getRoleName()
                .equalsIgnoreCase(originalUser.getJobRole().getRoleName())) {
            warnings.add("User's job role does not match the original user's job role.");
        }

        // 6️ Job level mismatch
        if (!coveringUser.getJobLevel().getLevelName()
                .equalsIgnoreCase(originalUser.getJobLevel().getLevelName())) {
            warnings.add("User's job level does not match the original user's job level.");
        }

        return warnings;
    }


    private boolean timeOverlap(LocalTime aStart, LocalTime aEnd, LocalTime bStart, LocalTime bEnd) {
        return !aEnd.isBefore(bStart) && !aStart.isAfter(bEnd);
    }
}
