package com.airport.admin.airport_admin.features.staff.shiftCover.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSummaryDto {
    private String firstName;
    private String lastName;
    private String email;

    // Display names
    private String roleName;
    private String jobRoleName;
    private String jobLevelName;
    private String constraintProfileName;
}
