package com.airport.admin.airport_admin.features.staff.shiftCover.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class CoverEligibilityCheckDto {
//    private Long originalUserId; will be using authentication principal
    private Long coveringUserId;
    private LocalDate shiftDate;
    private LocalTime startTime;
    private LocalTime endTime;
}
