package com.airport.admin.airport_admin.features.shiftCover.dto;

import com.airport.admin.airport_admin.enums.CoverRequestStatus;
import com.airport.admin.airport_admin.features.user.dto.UserResponseDto;
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
    private LocalDate shiftDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private CoverRequestStatus status;
}
