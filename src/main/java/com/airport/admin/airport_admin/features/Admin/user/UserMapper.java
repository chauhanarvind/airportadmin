package com.airport.admin.airport_admin.features.Admin.user;

import com.airport.admin.airport_admin.features.Admin.constraintProfile.ConstraintProfile;
import com.airport.admin.airport_admin.features.Admin.jobLevel.JobLevel;
import com.airport.admin.airport_admin.features.Admin.jobRole.JobRole;
import com.airport.admin.airport_admin.features.Admin.roles.Role;
import com.airport.admin.airport_admin.features.Admin.user.dto.CreateUserDto;
import com.airport.admin.airport_admin.features.Admin.user.dto.UpdateUserDto;
import com.airport.admin.airport_admin.features.Admin.user.dto.UserResponseDto;
import com.airport.admin.airport_admin.features.Admin.user.dto.UserSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    // CREATE
    public User toEntity(CreateUserDto dto, Role role, JobRole jobRole, JobLevel jobLevel, ConstraintProfile constraintProfile) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(role);
        user.setJobRole(jobRole);
        user.setJobLevel(jobLevel);
        user.setConstraintProfile(constraintProfile);
        return user;
    }

    // UPDATE (only set values if not null)
    public void updateEntity(User user, UpdateUserDto dto, Role role, JobRole jobRole, JobLevel jobLevel, ConstraintProfile constraintProfile) {
        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        // No password update here
        if (role != null) user.setRole(role);
        if (jobRole != null) user.setJobRole(jobRole);
        if (jobLevel != null) user.setJobLevel(jobLevel);
        user.setConstraintProfile(constraintProfile); // can be null intentionally
    }

    // RESPONSE DTO
    public UserResponseDto toDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());

        dto.setRoleId(user.getRole().getId());
        dto.setJobRoleId(user.getJobRole().getId());
        dto.setJobLevelId(user.getJobLevel().getId());
        dto.setConstraintProfileId(user.getConstraintProfile() != null
                ? user.getConstraintProfile().getId()
                : null);

        dto.setRoleName(user.getRole().getName());
        dto.setJobRoleName(user.getJobRole().getRoleName());
        dto.setJobLevelName(user.getJobLevel().getLevelName());
        dto.setConstraintProfileName(user.getConstraintProfile() != null
                ? user.getConstraintProfile().getName()
                : null);

        return dto;
    }

    // for drop downs we need it
    public UserSummaryDto toSummary(User user) {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String roleName = user.getRole().getName();
        return new UserSummaryDto(user.getId(), firstName, lastName, roleName);
    }


}
