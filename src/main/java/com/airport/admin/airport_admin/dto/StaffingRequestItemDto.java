package com.airport.admin.airport_admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffingRequestItemDto {
    @NotNull(message = "Job role is required")
    private Long job_role_id ;

    @NotNull(message = "Job level is required")
    private Long job_level_id;

    @NotNull(message = "Count is required")
    private Integer required_count;

    @NotBlank(message = "Start time is required")
    private String start_time; //"HH:MM" format

    @NotBlank(message = "End time is required")
    private String end_time; //"HH:MM" format
}
