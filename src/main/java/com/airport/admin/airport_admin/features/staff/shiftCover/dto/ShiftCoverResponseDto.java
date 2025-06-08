package com.airport.admin.airport_admin.features.staff.shiftCover.dto;

import com.airport.admin.airport_admin.enums.CoverRequestStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ShiftCoverResponseDto {

    private Long id;
    private UserSummaryDto originalUser;
    private UserSummaryDto coveringUser;

    private Long shiftId;
    private LocalDate shiftDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private CoverRequestStatus status;
}
