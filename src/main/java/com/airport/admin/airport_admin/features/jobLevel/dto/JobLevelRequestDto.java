package com.airport.admin.airport_admin.features.jobLevel.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobLevelRequestDto {
    @NotBlank(message = "Job level name is required")
    private String levelName;
}
