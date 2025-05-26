package com.airport.admin.airport_admin.services;

import com.airport.admin.airport_admin.enums.LeaveStatus;
import com.airport.admin.airport_admin.models.StaffAvailability;
import com.airport.admin.airport_admin.models.User;
import com.airport.admin.airport_admin.repositories.LeaveRequestRepository;
import com.airport.admin.airport_admin.repositories.RosterAssignmentRepository;
import com.airport.admin.airport_admin.repositories.StaffAvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CoverEligibilityService {

    private final LeaveRequestRepository leaveRepo;
    private final StaffAvailabilityRepository availabilityRepo;
    private final RosterAssignmentRepository rosterRepo;

    public List<String> checkWarnings(User coveringUser, LocalDate shiftDate, LocalTime startTime, LocalTime endTime) {
        List<String> warnings = new ArrayList<>();

        // 1️Leave check
        boolean onLeave = leaveRepo.existsByUserAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                coveringUser, LeaveStatus.APPROVED, shiftDate, shiftDate
        );
        if (onLeave) {
            warnings.add("User is on approved leave during this date.");
        }

        //  Availability check
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

        //  Roster conflict
        boolean hasConflict = rosterRepo.existsByUserAndShiftDateAndTimeOverlap(
                coveringUser, shiftDate, startTime, endTime
        );
        if (hasConflict) {
            warnings.add("User is already assigned to an overlapping shift.");
        }

        return warnings;
    }

    private boolean timeOverlap(LocalTime aStart, LocalTime aEnd, LocalTime bStart, LocalTime bEnd) {
        return !aEnd.isBefore(bStart) && !aStart.isAfter(bEnd);
    }
}
