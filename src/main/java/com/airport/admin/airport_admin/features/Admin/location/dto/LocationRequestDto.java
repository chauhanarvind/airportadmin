package com.airport.admin.airport_admin.features.Admin.location.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationRequestDto {
    @NotBlank(message = "Location name is required")
    private String locationName;

    private String description;
}
