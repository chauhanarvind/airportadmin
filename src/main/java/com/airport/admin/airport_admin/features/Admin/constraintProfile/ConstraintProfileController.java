package com.airport.admin.airport_admin.features.Admin.constraintProfile;

import com.airport.admin.airport_admin.features.Admin.constraintProfile.dto.ConstraintProfileRequestDto;
import com.airport.admin.airport_admin.features.Admin.constraintProfile.dto.ConstraintProfileResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/constraint-profiles") //can only be accessed by admin
public class ConstraintProfileController {

    private final ConstraintProfileService constraintProfileService;

    public ConstraintProfileController(ConstraintProfileService constraintProfileService) {
        this.constraintProfileService = constraintProfileService;
    }

    // to get all
    @GetMapping("/")
    public ResponseEntity<List<ConstraintProfileResponseDto>> getAllProfiles() {
        return ResponseEntity.ok(constraintProfileService.getAllProfiles());
    }

    // to get by id
    @GetMapping("/{id}")
    public ResponseEntity<ConstraintProfileResponseDto> getProfileById(@PathVariable Long id) {
        return ResponseEntity.ok(constraintProfileService.getProfileById(id));
    }

    // to create
    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/create")
    public ResponseEntity<ConstraintProfileResponseDto> createProfile(
            @Valid @RequestBody ConstraintProfileRequestDto dto
    ) {
        return ResponseEntity.ok(constraintProfileService.createProfile(dto));
    }

    //to update by id
    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/{id}")
    public ResponseEntity<ConstraintProfileResponseDto> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody ConstraintProfileRequestDto dto
    ) {
        return ResponseEntity.ok(constraintProfileService.updateProfile(id, dto));
    }

    // to delete
    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        constraintProfileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}
