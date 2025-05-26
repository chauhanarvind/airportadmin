package com.airport.admin.airport_admin.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class RosterAssignmentDto {
    private Long id;

    private LocalDate date;

    private String userFullName;
    private String roleName;

    private LocalTime startTime;
    private LocalTime endTime;

    private String locationName;
}
