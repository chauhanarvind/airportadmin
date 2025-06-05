package com.airport.admin.airport_admin.features.staff.staffing.dto.StaffingRequestItem;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class StaffingRequestItemCreateDto {

    @NotNull(message = "job role is required")
    private Long jobRoleId;

    @NotNull(message = "job level is required")
    private Long jobLevelId;

    @NotNull(message = "required count is required")
    private Integer requiredCount;

    @NotNull(message = "start time is required")
    private LocalTime startTime;

    @NotNull(message = "end time is required")
    private LocalTime endTime;


}
