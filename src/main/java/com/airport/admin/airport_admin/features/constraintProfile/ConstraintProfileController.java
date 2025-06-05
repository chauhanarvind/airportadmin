package com.airport.admin.airport_admin.features.constraintProfile;

import com.airport.admin.airport_admin.features.constraintProfile.dto.ConstraintProfileRequestDto;
import com.airport.admin.airport_admin.features.constraintProfile.dto.ConstraintProfileResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/constraint-profiles")

public class ConstraintProfileController {

    private final ConstraintProfileService constraintProfileService;

    public ConstraintProfileController(ConstraintProfileService constraintProfileService) {
        this.constraintProfileService = constraintProfileService;
    }

    @GetMapping("/")
    public ResponseEntity<List<ConstraintProfileResponseDto>> getAllProfiles() {
        return ResponseEntity.ok(constraintProfileService.getAllProfiles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConstraintProfileResponseDto> getProfileById(@PathVariable Long id) {
        return ResponseEntity.ok(constraintProfileService.getProfileById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<ConstraintProfileResponseDto> createProfile(
            @Valid @RequestBody ConstraintProfileRequestDto dto
    ) {
        return ResponseEntity.ok(constraintProfileService.createProfile(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConstraintProfileResponseDto> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody ConstraintProfileRequestDto dto
    ) {
        return ResponseEntity.ok(constraintProfileService.updateProfile(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        constraintProfileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}
