package com.airport.admin.airport_admin.features.staff.shiftCover.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoverEligibilityCheckDto {

    @NotNull(message = "Covering user ID is required")
    private Long coveringUserId;

    @NotNull(message = "Shift ID is required")
    private Long shiftId;
}
