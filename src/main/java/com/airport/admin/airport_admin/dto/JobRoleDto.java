package com.airport.admin.airport_admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobRoleDto {
    private Long id;

    @NotBlank(message = "Job role name is required")
    private String roleName;

    @NotBlank(message = "Job category id is required")
    private Long categoryId;
}
