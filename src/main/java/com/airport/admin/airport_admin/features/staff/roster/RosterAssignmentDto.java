package com.airport.admin.airport_admin.features.staff.roster;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class RosterAssignmentDto {

    private Long id;

    @NotNull(message = "date is required")
    private LocalDate date;

    private String userFullName;

    private String roleName;

    @NotNull(message = "start time is required")
    private LocalTime startTime;

    @NotNull(message = "end time is required")
    private LocalTime endTime;

    private String locationName;

    private boolean unassigned;
}
