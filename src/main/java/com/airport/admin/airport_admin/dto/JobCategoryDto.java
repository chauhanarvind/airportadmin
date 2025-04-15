package com.airport.admin.airport_admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobCategoryDto {
    private Long id;

    @NotBlank(message = "Category name is required")
    private String categoryName;
}
