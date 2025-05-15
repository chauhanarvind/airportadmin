package com.airport.admin.airport_admin.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ShiftSwapRequestDto {

    @NotNull(message = "Requested user ID is required")
    private Long requestedUserId;

    @NotNull(message = "Shift date is required")
    @FutureOrPresent(message = "Shift date must be today or in the future")
    private LocalDate shiftDate;

    @Size(max = 300, message = "Reason must be under 300 characters")
    private String reason;
}
