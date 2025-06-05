package com.airport.admin.airport_admin.features.Admin.location;

import com.airport.admin.airport_admin.features.Admin.location.dto.LocationRequestDto;
import com.airport.admin.airport_admin.features.Admin.location.dto.LocationResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class LocationMapper {

    public static Location toEntity(LocationRequestDto dto) {
        Location location = new Location();
        location.setLocationName(dto.getLocationName());
        location.setDescription(dto.getDescription());
        return location;
    }

    public static void updateEntity(Location location, LocationRequestDto dto) {
        location.setLocationName(dto.getLocationName());
        location.setDescription(dto.getDescription());
    }

    public static LocationResponseDto toDto(Location location) {
        LocationResponseDto dto = new LocationResponseDto();
        dto.setId(location.getId());
        dto.setLocationName(location.getLocationName());
        dto.setDescription(location.getDescription());
        return dto;
    }

    public static List<LocationResponseDto> toDtoList(List<Location> locations) {
        return locations.stream().map(LocationMapper::toDto).collect(Collectors.toList());
    }
}
