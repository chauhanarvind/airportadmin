package com.airport.admin.airport_admin.features.staff.staffAvailability;

import com.airport.admin.airport_admin.features.Admin.user.User;
import com.airport.admin.airport_admin.features.staff.staffAvailability.dto.StaffAvailabilityRequestDto;
import com.airport.admin.airport_admin.features.staff.staffAvailability.dto.StaffAvailabilityResponseDto;
import com.airport.admin.airport_admin.security.SecurityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/staff-availability")
@RequiredArgsConstructor
public class StaffAvailabilityController {

    private final StaffAvailabilityService staffAvailabilityService;
    private final SecurityService securityService;


    @GetMapping
    @PreAuthorize("!hasAnyRole('Crew')")
    public ResponseEntity<Page<StaffAvailabilityResponseDto>> getFilteredAvailability(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) LocalDate date,
            Pageable pageable
    ) {
        Page<StaffAvailabilityResponseDto> page = staffAvailabilityService.getFilteredAvailability(userId, date, pageable);
        return ResponseEntity.ok(page);
    }

    // Create availability for the logged-in user
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StaffAvailabilityResponseDto> createAvailability(
            @Valid @RequestBody StaffAvailabilityRequestDto dto
    ) {
        Long userId = securityService.getAuthenticatedUserId();
        return ResponseEntity.ok(staffAvailabilityService.createAvailability(userId, dto));
    }
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StaffAvailabilityResponseDto> updateAvailability(
            @PathVariable Long id,
            @Valid @RequestBody StaffAvailabilityRequestDto dto
    ) {
        Long userId = securityService.getAuthenticatedUserId();
        return ResponseEntity.ok(staffAvailabilityService.updateAvailability(id,userId, dto));
    }



    // Get all availability entries for a user (ownership check)
    @GetMapping("/user/{userId}")
    @PreAuthorize("@securityService.isOwner(#userId, authentication)")
    public ResponseEntity<List<StaffAvailabilityResponseDto>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(staffAvailabilityService.getAvailabilityByUser(userId));
    }

    // Get a single availability entry
    @GetMapping("/{id}")
    @PreAuthorize("@availabilitySecurity.canView(#id, authentication) or hasRole('Admin')")
    public ResponseEntity<StaffAvailabilityResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(staffAvailabilityService.getById(id));
    }

    // Delete an entry (owner or admin)
    @DeleteMapping("/{id}")
    @PreAuthorize("@availabilitySecurity.canModify(#id, authentication) or hasRole('Admin')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        staffAvailabilityService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
