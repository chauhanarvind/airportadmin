package com.airport.admin.airport_admin.features.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDto {

    private Long id;

    private String firstName;

    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    private Long roleId;

    private Long jobRoleId;

    private Long jobLevelId;

    private Long constraintProfileId;
}
