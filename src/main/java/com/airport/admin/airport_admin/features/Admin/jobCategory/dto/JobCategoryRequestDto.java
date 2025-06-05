package com.airport.admin.airport_admin.features.Admin.jobCategory.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobCategoryRequestDto {
    @NotBlank(message = "category name is required")
    private String categoryName;
}
