package com.airport.admin.airport_admin.features.shiftCover.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class CoverEligibilityCheckDto {
    private Long coveringUserId;
    private LocalDate shiftDate;
    private LocalTime startTime;
    private LocalTime endTime;

}
