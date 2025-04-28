package com.airport.admin.airport_admin.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid format")
    private String email;

//    @NotBlank(message = "Password is required")
//    no need for the above here because we are
//    validating this on create service layer
//    and updating it on update
    @Size(min=6, message = "Password must be at least 6 characters long")
    private String password;

    @NotNull(message = "Role id cannot be null")
    private Long roleId;

    @NotNull(message = "Job role id cannot be null")
    private Long jobRoleId;

    @NotNull(message = "Job Level id cannot be null")
    private Long jobLevelId;
}
