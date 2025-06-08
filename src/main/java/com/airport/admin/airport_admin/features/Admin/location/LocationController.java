package com.airport.admin.airport_admin.features.Admin.location;

import com.airport.admin.airport_admin.features.Admin.location.dto.LocationRequestDto;
import com.airport.admin.airport_admin.features.Admin.location.dto.LocationResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
 //can only be accessed by admin
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    // to get all
    @GetMapping("/")
    public ResponseEntity<List<LocationResponseDto>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    // get by id
    @GetMapping("/{id}")
    public ResponseEntity<LocationResponseDto> getLocationById(@PathVariable Long id) {
        return ResponseEntity.ok(locationService.getLocationById(id));
    }

    // to create
    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/create")
    public ResponseEntity<LocationResponseDto> createLocation(
            @Valid @RequestBody LocationRequestDto dto
    ) {
        return ResponseEntity.ok(locationService.createLocation(dto));
    }

    // to update by id
    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/{id}")
    public ResponseEntity<LocationResponseDto> updateLocation(
            @PathVariable Long id,
            @Valid @RequestBody LocationRequestDto dto
    ) {
        return ResponseEntity.ok(locationService.updateLocation(id, dto));
    }

    // to delete by id
    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
