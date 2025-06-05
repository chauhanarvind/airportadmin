package com.airport.admin.airport_admin.features.Admin.constraintProfile;

import com.airport.admin.airport_admin.features.Admin.constraintProfile.dto.ConstraintProfileRequestDto;
import com.airport.admin.airport_admin.features.Admin.constraintProfile.dto.ConstraintProfileResponseDto;

import java.util.List;
import java.util.stream.Collectors;


public class ConstraintProfileMapper {

    public static ConstraintProfile toEntity(ConstraintProfileRequestDto dto) {
        ConstraintProfile profile = new ConstraintProfile();
        profile.setName(dto.getName());
        profile.setMaxHoursPerDay(dto.getMaxHoursPerDay());
        profile.setMaxHoursPerWeek(dto.getMaxHoursPerWeek());
        profile.setPreferredStartTime(dto.getPreferredStartTime());
        profile.setPreferredEndTime(dto.getPreferredEndTime());
        profile.setAllowedDays(dto.getAllowedDays());
        return profile;
    }

    public static void updateEntity(ConstraintProfile profile, ConstraintProfileRequestDto dto) {
        profile.setName(dto.getName());
        profile.setMaxHoursPerDay(dto.getMaxHoursPerDay());
        profile.setMaxHoursPerWeek(dto.getMaxHoursPerWeek());
        profile.setPreferredStartTime(dto.getPreferredStartTime());
        profile.setPreferredEndTime(dto.getPreferredEndTime());
        profile.setAllowedDays(dto.getAllowedDays());
    }

    public static ConstraintProfileResponseDto toDto(ConstraintProfile profile) {
        ConstraintProfileResponseDto dto = new ConstraintProfileResponseDto();
        dto.setId(profile.getId());
        dto.setName(profile.getName());
        dto.setMaxHoursPerDay(profile.getMaxHoursPerDay());
        dto.setMaxHoursPerWeek(profile.getMaxHoursPerWeek());
        dto.setPreferredStartTime(profile.getPreferredStartTime());
        dto.setPreferredEndTime(profile.getPreferredEndTime());
        dto.setAllowedDays(profile.getAllowedDays());
        return dto;
    }

    public static List<ConstraintProfileResponseDto> toDtoList(List<ConstraintProfile> profiles) {
        return profiles.stream().map(ConstraintProfileMapper::toDto).collect(Collectors.toList());
    }
}
