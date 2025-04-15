package com.airport.admin.airport_admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobLevelDto {
    private Long id;

    @NotBlank(message = "Job level name is required")
    private String levelName;
}
