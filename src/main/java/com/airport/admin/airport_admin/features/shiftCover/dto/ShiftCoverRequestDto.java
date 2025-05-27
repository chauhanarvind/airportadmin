package com.airport.admin.airport_admin.features.shiftCover.dto;

import com.airport.admin.airport_admin.enums.CoverRequestStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ShiftCoverRequestDto {

    private Long id;
    private Long originalUserId;
    private Long coveringUserId;
    private LocalDate shiftDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private CoverRequestStatus status;
    private String warnings;
}
