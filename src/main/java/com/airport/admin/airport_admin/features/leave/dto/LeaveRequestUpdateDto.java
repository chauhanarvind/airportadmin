package com.airport.admin.airport_admin.features.leave.dto;

import com.airport.admin.airport_admin.enums.LeaveStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaveRequestUpdateDto {
    @NotNull(message = "Leave status is required")
    private LeaveStatus status;
}
