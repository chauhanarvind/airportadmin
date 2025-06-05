package com.airport.admin.airport_admin.features.staff.staffAvailability.dto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class StaffAvailabilityResponseDto {

    private Long id;
    private Long userId;
    private String userName; // Optional, for display
    private LocalDate date;

    private LocalTime unavailableFrom;
    private LocalTime unavailableTo;

    private boolean isAvailable;
}
