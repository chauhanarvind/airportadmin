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

    @NotNull(message = "user id is required")
    private Long userId;

    @NotNull(message = "date is required")
    private LocalDate date;

    private LocalTime unavailableFrom;
    private LocalTime unavailableTo;

    @NotNull(message = "availability status is required")
    private Boolean isAvailable;
}
