package com.airport.admin.airport_admin.features.staff.shiftCover.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShiftCoverRequestCreateDto {

    @NotNull(message = "covering user ID is required")
    private Long coveringUserId;

    @NotNull(message = "shift ID is required")
    private Long shiftId;
}
