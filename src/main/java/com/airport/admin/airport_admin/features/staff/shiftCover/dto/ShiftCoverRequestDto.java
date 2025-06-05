package com.airport.admin.airport_admin.features.staff.shiftCover.dto;

import com.airport.admin.airport_admin.enums.CoverRequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ShiftCoverRequestDto {

    private Long id;

    @NotNull(message = "original user id is required")
    private Long originalUserId;

    @NotNull(message = "covering user id is required")
    private Long coveringUserId;

    @NotNull(message = "shift date is required")
    private LocalDate shiftDate;

    @NotNull(message = "start time is required")
    private LocalTime startTime;

    @NotNull(message = "end time is required")
    private LocalTime endTime;

    @NotNull(message = "status is required")
    private CoverRequestStatus status;
}