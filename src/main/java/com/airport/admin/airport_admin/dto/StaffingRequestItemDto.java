package com.airport.admin.airport_admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffingRequestItemDto {
    @NotNull(message = "Job role is required")
    private Long jobRoleId ;

    @NotNull(message = "Job level is required")
    private Long jobLevelId;

    @NotNull(message = "Count is required")
    private Integer requiredCount;

    @NotBlank(message = "Start time is required")
    private String startTime; //"HH:MM" format

    @NotBlank(message = "End time is required")
    private String endTime; //"HH:MM" format
}
