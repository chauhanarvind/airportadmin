package com.airport.admin.airport_admin.features.shiftCover;

import com.airport.admin.airport_admin.features.shiftCover.dto.CoverEligibilityCheckDto;
import com.airport.admin.airport_admin.features.shiftCover.dto.ShiftCoverRequestDto;
import com.airport.admin.airport_admin.features.shiftCover.service.CoverEligibilityService;
import com.airport.admin.airport_admin.features.shiftCover.service.ShiftCoverRequestService;
import com.airport.admin.airport_admin.features.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cover-requests")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('Admin', 'Supervisor', 'Staff')")
public class ShiftCoverRequestController {

    private final ShiftCoverRequestService coverRequestService;
    private final CoverEligibilityService coverEligibilityService;
    private final UserRepository userRepo;

    // 📥 Submit a new shift cover request
    @PostMapping
    public ResponseEntity<ShiftCoverRequestDto> submitRequest(@RequestBody ShiftCoverRequestDto dto) {
        ShiftCoverRequestDto saved = coverRequestService.submitCoverRequest(dto);
        return ResponseEntity.ok(saved);
    }

    // 📄 Get all cover requests
    @GetMapping
    public List<ShiftCoverRequestDto> getAllRequests() {
        return coverRequestService.getAllRequests();
    }

    // 📄 Get only pending requests
    @GetMapping("/pending")
    public List<ShiftCoverRequestDto> getPendingRequests() {
        return coverRequestService.getPendingRequests();
    }

    // ❗Check warnings before submitting (frontend pre-check)
    @PostMapping("/check")
    public ResponseEntity<List<String>> checkCoverEligibility(@RequestBody CoverEligibilityCheckDto dto) {
        return ResponseEntity.ok(coverRequestService.getApprovalWarnings(dto));
    }

    // ❗Check warnings for a submitted request (admin view)
    @GetMapping("/{id}/warnings")
    public ResponseEntity<List<String>> getRequestWarnings(@PathVariable Long id) {
        return ResponseEntity.ok(coverRequestService.getApprovalWarnings(id));
    }

    // ✅ Approve a pending cover request
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('Admin', 'Supervisor')")
    public ResponseEntity<Void> approveRequest(@PathVariable Long id) {
        coverRequestService.approveRequest(id);
        return ResponseEntity.ok().build();
    }

    // ❌ Reject a pending cover request
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('Admin', 'Supervisor')")
    public ResponseEntity<Void> rejectRequest(@PathVariable Long id) {
        coverRequestService.rejectRequest(id);
        return ResponseEntity.ok().build();
    }
}
