package com.airport.admin.airport_admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class RoleDto{
    private Long id;

    @NotBlank(message = "Role name is required")
    private String name;
}
