package com.airport.admin.airport_admin.features.jobRole;

import com.airport.admin.airport_admin.features.jobCategory.JobCategory;
import com.airport.admin.airport_admin.features.jobRole.dto.JobRoleRequestDto;
import com.airport.admin.airport_admin.features.jobRole.dto.JobRoleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JobRoleMapper {

    public static JobRole toEntity(JobRoleRequestDto dto, JobCategory category) {
        JobRole jobRole = new JobRole();
        jobRole.setRoleName(dto.getRoleName());
        jobRole.setCategory(category);
        return jobRole;
    }

    public static void updateEntity(JobRole jobRole, JobRoleRequestDto dto, JobCategory category) {
        jobRole.setRoleName(dto.getRoleName());
        jobRole.setCategory(category);
    }

    public static JobRoleResponseDto toDto(JobRole entity) {
        JobRoleResponseDto dto = new JobRoleResponseDto();
        dto.setId(entity.getId());
        dto.setRoleName(entity.getRoleName());
        dto.setCategoryId(entity.getCategory().getId());
        dto.setCategoryName(entity.getCategory().getCategoryName());
        return dto;
    }

    public static List<JobRoleResponseDto> toDtoList(List<JobRole> entities) {
        return entities.stream().map(JobRoleMapper::toDto).collect(Collectors.toList());
    }
}
