package com.airport.admin.airport_admin.features.user.dto;

import lombok.Getter;
import lombok.Setter;

// dto for get requests
@Getter
@Setter
public class UserResponseDto {
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    // Raw IDs (needed for update forms)
    private Long roleId;
    private Long jobRoleId;
    private Long jobLevelId;
    private Long constraintProfileId;

    // Display names
    private String roleName;
    private String jobRoleName;
    private String jobLevelName;
    private String constraintProfileName;
}
