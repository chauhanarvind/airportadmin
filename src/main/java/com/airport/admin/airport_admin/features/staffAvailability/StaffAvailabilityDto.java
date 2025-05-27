package com.airport.admin.airport_admin.features.staffAvailability;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class StaffAvailabilityDto {

    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private LocalTime unavailableFrom;

    private LocalTime unavailableTo;

    @NotNull(message = "Availability flag is required")
    private Boolean isAvailable;
}
