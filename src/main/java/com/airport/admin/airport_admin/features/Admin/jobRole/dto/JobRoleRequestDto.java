package com.airport.admin.airport_admin.features.Admin.jobRole.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

// Request DTO: for create/update
@Getter
@Setter
public class JobRoleRequestDto {
    @NotBlank(message = "job role name is required")
    private String roleName;

    @NotNull(message = "job category id is required")
    private Long categoryId;
}
