package com.airport.admin.airport_admin.features.location;

import com.airport.admin.airport_admin.features.location.dto.LocationRequestDto;
import com.airport.admin.airport_admin.features.location.dto.LocationResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/")
    public ResponseEntity<List<LocationResponseDto>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationResponseDto> getLocationById(@PathVariable Long id) {
        return ResponseEntity.ok(locationService.getLocationById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<LocationResponseDto> createLocation(
            @Valid @RequestBody LocationRequestDto dto
    ) {
        return ResponseEntity.ok(locationService.createLocation(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationResponseDto> updateLocation(
            @PathVariable Long id,
            @Valid @RequestBody LocationRequestDto dto
    ) {
        return ResponseEntity.ok(locationService.updateLocation(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
