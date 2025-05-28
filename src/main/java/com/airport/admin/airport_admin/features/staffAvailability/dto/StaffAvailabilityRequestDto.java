package com.airport.admin.airport_admin.features.staffAvailability.dto;

import com.airport.admin.airport_admin.features.staffAvailability.validation.ValidTimeRange;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@ValidTimeRange
public class StaffAvailabilityRequestDto {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private LocalTime unavailableFrom;
    private LocalTime unavailableTo;

    @NotNull(message = "Availability status is required")
    private Boolean isAvailable;
}
