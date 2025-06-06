package com.airport.admin.airport_admin.features.staff.staffAvailability.dto;

import com.airport.admin.airport_admin.features.staff.staffAvailability.validation.ValidTimeRange;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@ValidTimeRange
public class StaffAvailabilityRequestDto {

    // will be using authentication principal to get user id
//    @NotNull(message = "user id is required")
//    private Long userId;

    @NotNull(message = "date is required")
    private LocalDate date;

    private LocalTime unavailableFrom;
    private LocalTime unavailableTo;

    @NotNull(message = "availability status is required")
    private Boolean isAvailable;
}
