package com.airport.admin.airport_admin.features.staff.leave.dto;

import com.airport.admin.airport_admin.enums.LeaveStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
public class LeaveRequestGetDto {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveStatus status;
    private Long userId;
    private String userName;      // Optional for display
    private String reason;
    private OffsetDateTime createdAt;
}
