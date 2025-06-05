package com.airport.admin.airport_admin.features.staffAvailability;

import com.airport.admin.airport_admin.features.staffAvailability.dto.StaffAvailabilityRequestDto;
import com.airport.admin.airport_admin.features.staffAvailability.dto.StaffAvailabilityResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff-availability")
public class StaffAvailabilityController {

    private final StaffAvailabilityService staffAvailabilityService;

    public StaffAvailabilityController(StaffAvailabilityService staffAvailabilityService) {
        this.staffAvailabilityService = staffAvailabilityService;
    }

    @GetMapping("/")
    public ResponseEntity<List<StaffAvailabilityResponseDto>> getAllAvailability() {
        return ResponseEntity.ok(staffAvailabilityService.getAllAvailability());
    }


    // Create or update availability for a user
    @PostMapping
    public ResponseEntity<StaffAvailabilityResponseDto> saveAvailability(
            @Valid @RequestBody StaffAvailabilityRequestDto dto
    ) {
        return ResponseEntity.ok(staffAvailabilityService.saveAvailability(dto));
    }

    // Get all availability entries for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<StaffAvailabilityResponseDto>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(staffAvailabilityService.getAvailabilityByUser(userId));
    }


    // Get single availability entry by ID
    @GetMapping("/{id}")
    public ResponseEntity<StaffAvailabilityResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(staffAvailabilityService.getById(id));
    }

    // Delete an entry
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        staffAvailabilityService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
