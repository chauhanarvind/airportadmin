package com.airport.admin.airport_admin.features.staff.staffing.dto.StaffingRequest;

import com.airport.admin.airport_admin.enums.RosterStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


//to update status
@Getter
@Setter
public class StaffingRequestUpdateDto {
    @NotNull(message = "status is required")
    private RosterStatus status;
}
