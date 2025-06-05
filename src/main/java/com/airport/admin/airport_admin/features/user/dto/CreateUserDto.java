package com.airport.admin.airport_admin.features.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// dto to create
@Getter
@Setter
public class CreateUserDto {

    @NotBlank(message = "first name is required")
    private String firstName;

    @NotBlank(message = "last name is required")
    private String lastName;

    @NotBlank(message = "email is required")
    @Email(message = "invalid email format")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 6, message = "password must be at least 6 characters long")
    private String password;

    @NotNull(message = "role ID is required")
    private Long roleId;

    @NotNull(message = "job role ID is required")
    private Long jobRoleId;

    @NotNull(message = "job level ID is required")
    private Long jobLevelId;

    private Long constraintProfileId;
}
