package com.airport.admin.airport_admin.features.Admin.jobLevel;


import com.airport.admin.airport_admin.features.Admin.jobLevel.dto.JobLevelRequestDto;
import com.airport.admin.airport_admin.features.Admin.jobLevel.dto.JobLevelResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class JobLevelMapper {

    public static JobLevel toEntity(JobLevelRequestDto dto) {
        JobLevel jobLevel = new JobLevel();
        jobLevel.setLevelName(dto.getLevelName());
        return jobLevel;
    }

    public static void updateEntity(JobLevel jobLevel, JobLevelRequestDto dto) {
        jobLevel.setLevelName(dto.getLevelName());
    }

    public static JobLevelResponseDto toDto(JobLevel entity) {
        JobLevelResponseDto dto = new JobLevelResponseDto();
        dto.setId(entity.getId());
        dto.setLevelName(entity.getLevelName());
        return dto;
    }

    public static List<JobLevelResponseDto> toDtoList(List<JobLevel> entities) {
        return entities.stream()
                .map(JobLevelMapper::toDto)
                .collect(Collectors.toList());
    }
}
