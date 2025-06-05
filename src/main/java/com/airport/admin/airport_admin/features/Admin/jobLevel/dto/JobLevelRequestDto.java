package com.airport.admin.airport_admin.features.Admin.jobLevel.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobLevelRequestDto {
    @NotBlank(message = "job level name is required")
    private String levelName;
}
