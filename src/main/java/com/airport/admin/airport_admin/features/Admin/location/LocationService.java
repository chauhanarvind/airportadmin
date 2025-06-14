package com.airport.admin.airport_admin.features.Admin.location;

import com.airport.admin.airport_admin.features.Admin.location.dto.LocationRequestDto;
import com.airport.admin.airport_admin.features.Admin.location.dto.LocationResponseDto;
import com.airport.admin.airport_admin.utils.DuplicateResourceException;
import com.airport.admin.airport_admin.utils.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<LocationResponseDto> getAllLocations() {
        List<Location> locations = locationRepository.findAll();
        return LocationMapper.toDtoList(locations);
    }

    public LocationResponseDto getLocationById(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location does not exist"));
        return LocationMapper.toDto(location);
    }

    public LocationResponseDto createLocation(LocationRequestDto dto) {
        Optional<Location> existing = locationRepository.findByLocationName(dto.getLocationName());
        if (existing.isPresent()) {
            throw new DuplicateResourceException("Location with this name already exists");
        }

        Location location = LocationMapper.toEntity(dto);
        location = locationRepository.save(location);
        return LocationMapper.toDto(location);
    }

    public LocationResponseDto updateLocation(Long id, LocationRequestDto dto) {
        Location existing = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location does not exist"));

        LocationMapper.updateEntity(existing, dto);
        existing = locationRepository.save(existing);
        return LocationMapper.toDto(existing);
    }

    public void deleteLocation(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location does not exist"));
        locationRepository.delete(location);
    }
}
