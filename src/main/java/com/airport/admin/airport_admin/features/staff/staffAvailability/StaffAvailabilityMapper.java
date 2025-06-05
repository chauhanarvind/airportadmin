package com.airport.admin.airport_admin.features.staff.staffAvailability;

import com.airport.admin.airport_admin.features.staff.staffAvailability.dto.StaffAvailabilityRequestDto;
import com.airport.admin.airport_admin.features.staff.staffAvailability.dto.StaffAvailabilityResponseDto;
import com.airport.admin.airport_admin.features.Admin.user.User;

import java.util.List;
import java.util.stream.Collectors;

public class StaffAvailabilityMapper {

    public static StaffAvailability toEntity(StaffAvailabilityRequestDto dto, User user) {
        StaffAvailability availability = new StaffAvailability();
        availability.setUser(user);
        availability.setDate(dto.getDate());
        availability.setUnavailableFrom(dto.getUnavailableFrom());
        availability.setUnavailableTo(dto.getUnavailableTo());
        availability.setAvailable(dto.getIsAvailable());
        return availability;
    }

    public static void updateEntity(StaffAvailability entity, StaffAvailabilityRequestDto dto, User user) {
        entity.setUser(user);
        entity.setDate(dto.getDate());
        entity.setUnavailableFrom(dto.getUnavailableFrom());
        entity.setUnavailableTo(dto.getUnavailableTo());
        entity.setAvailable(dto.getIsAvailable());
    }

    public static StaffAvailabilityResponseDto toDto(StaffAvailability entity) {
        StaffAvailabilityResponseDto dto = new StaffAvailabilityResponseDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser().getId());
        dto.setUserName(entity.getUser().getFirstName() + " " + entity.getUser().getLastName()); // Or .getName()
        dto.setDate(entity.getDate());
        dto.setUnavailableFrom(entity.getUnavailableFrom());
        dto.setUnavailableTo(entity.getUnavailableTo());
        dto.setAvailable(entity.isAvailable());
        return dto;
    }

    public static List<StaffAvailabilityResponseDto> toDtoList(List<StaffAvailability> list) {
        return list.stream().map(StaffAvailabilityMapper::toDto).collect(Collectors.toList());
    }
}
