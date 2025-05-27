package com.airport.admin.airport_admin.features.jobRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobRoleDto {
    private Long id;

    @NotBlank(message = "Job role name is required")
    private String roleName;

    @NotNull(message = "Job category id is required")
    private Long categoryId;
}
