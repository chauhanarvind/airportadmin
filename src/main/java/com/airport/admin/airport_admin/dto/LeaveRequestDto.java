package com.airport.admin.airport_admin.dto;

import com.airport.admin.airport_admin.enums.LeaveStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
public class LeaveRequestDto {

    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;

    private String reason;

    private LeaveStatus status;

    private Long userId;
    private String userFullName;

    private OffsetDateTime createdAt;
}
