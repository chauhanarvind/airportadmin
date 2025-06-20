package com.airport.admin.airport_admin.features.staff.leave.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LeaveRequestCreateDto {

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    //will be using authentication principal to get user id
//    @NotNull(message = "User ID is required")
//    private Long userId;

    @NotBlank(message = "Reason is required")
    private String reason;
}
