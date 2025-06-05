package com.airport.admin.airport_admin.features.Admin.jobCategory;


import com.airport.admin.airport_admin.features.Admin.jobCategory.dto.JobCategoryRequestDto;
import com.airport.admin.airport_admin.features.Admin.jobCategory.dto.JobCategoryResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class JobCategoryMapper {

    public static JobCategory toEntity(JobCategoryRequestDto dto) {
        JobCategory category = new JobCategory();
        category.setCategoryName(dto.getCategoryName());
        return category;
    }

    public static void updateEntity(JobCategory category, JobCategoryRequestDto dto) {
        category.setCategoryName(dto.getCategoryName());
    }

    public static JobCategoryResponseDto toDto(JobCategory entity) {
        JobCategoryResponseDto dto = new JobCategoryResponseDto();
        dto.setId(entity.getId());
        dto.setCategoryName(entity.getCategoryName());
        return dto;
    }

    public static List<JobCategoryResponseDto> toDtoList(List<JobCategory> entities) {
        return entities.stream()
                .map(JobCategoryMapper::toDto)
                .collect(Collectors.toList());
    }
}
