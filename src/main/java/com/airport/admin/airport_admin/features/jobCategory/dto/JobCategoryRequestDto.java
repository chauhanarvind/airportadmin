package com.airport.admin.airport_admin.features.jobCategory.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobCategoryRequestDto {
    @NotBlank(message = "Category name is required")
    private String categoryName;
}
