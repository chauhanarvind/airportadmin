package com.airport.admin.airport_admin.features.Admin.roles;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RoleDto{
    private Long id;

    @NotBlank(message = "role name is required")
    private String name;
}
