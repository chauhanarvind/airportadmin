package com.airport.admin.airport_admin.features.location;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDto {
    private Long id;

    @NotBlank(message = "Location name is required")
    private String locationName;
    private String description;
}
