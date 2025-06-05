package com.airport.admin.airport_admin.features.Admin.jobRole.dto;

import lombok.Getter;
import lombok.Setter;

// Response DTO: for frontend display
@Getter
@Setter
public class JobRoleResponseDto {
    private Long id;
    private String roleName;
    private Long categoryId;
    private String categoryName; // helpful for display
}
