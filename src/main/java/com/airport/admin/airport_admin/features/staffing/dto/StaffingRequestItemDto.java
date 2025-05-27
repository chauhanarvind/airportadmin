package com.airport.admin.airport_admin.features.staffing.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class StaffingRequestItemDto {
    @NotNull(message = "Job role is required")
    private Long jobRoleId ;

    @NotNull(message = "Job level is required")
    private Long jobLevelId;

    @NotNull(message = "Count is required")
    private Integer requiredCount;

    @NotNull(message = "Start time is required")
    private LocalTime startTime; //"HH:MM" format

    @NotNull(message = "End time is required")
    private LocalTime endTime; //"HH:MM" format
}
