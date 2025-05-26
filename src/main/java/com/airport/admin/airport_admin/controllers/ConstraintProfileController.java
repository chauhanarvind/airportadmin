package com.airport.admin.airport_admin.controllers;

import com.airport.admin.airport_admin.dto.ConstraintProfileDto;
import com.airport.admin.airport_admin.models.ConstraintProfile;
import com.airport.admin.airport_admin.services.ConstraintProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/constraint-profiles")
public class ConstraintProfileController {

    private final ConstraintProfileService constraintProfileService;

    public ConstraintProfileController(ConstraintProfileService constraintProfileService) {
        this.constraintProfileService = constraintProfileService;
    }

    @GetMapping
    public List<ConstraintProfile> getAllProfiles() {
        return constraintProfileService.getAllProfiles();
    }

    @PostMapping
    public ResponseEntity<ConstraintProfile> create(@RequestBody ConstraintProfileDto dto) {
        return ResponseEntity.ok(constraintProfileService.createProfile(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConstraintProfile> update(@PathVariable Long id, @RequestBody ConstraintProfileDto dto) {
        return ResponseEntity.ok(constraintProfileService.updateProfile(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        constraintProfileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConstraintProfile> getById(@PathVariable Long id) {
        return constraintProfileService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
