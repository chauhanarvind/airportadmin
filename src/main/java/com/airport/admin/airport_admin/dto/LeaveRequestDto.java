package com.airport.admin.airport_admin.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveRequestDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
}
