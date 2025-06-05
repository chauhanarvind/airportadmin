package com.airport.admin.airport_admin.features.shiftCover;

import com.airport.admin.airport_admin.features.shiftCover.dto.CoverEligibilityCheckDto;
import com.airport.admin.airport_admin.features.shiftCover.dto.ShiftCoverRequestDto;
import com.airport.admin.airport_admin.features.shiftCover.dto.ShiftCoverResponseDto;
import com.airport.admin.airport_admin.features.shiftCover.service.CoverEligibilityService;
import com.airport.admin.airport_admin.features.shiftCover.service.ShiftCoverRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cover-requests")
@RequiredArgsConstructor
public class ShiftCoverRequestController {

    private final ShiftCoverRequestService coverRequestService;
    private final CoverEligibilityService coverEligibilityService;

    // 📥 Submit a new shift cover request
    @PostMapping
    public ResponseEntity<ShiftCoverResponseDto> submitRequest(@RequestBody ShiftCoverRequestDto dto) {
        ShiftCoverResponseDto saved = coverRequestService.submitCoverRequest(dto);
        return ResponseEntity.ok(saved);
    }

    // 📤 Get all cover requests
    @GetMapping
    public ResponseEntity<List<ShiftCoverResponseDto>> getAllRequests() {
        return ResponseEntity.ok(coverRequestService.getAllRequests());
    }

    // 📤 Get only pending requests
    @GetMapping("/pending")
    public ResponseEntity<List<ShiftCoverResponseDto>> getPendingRequests() {
        return ResponseEntity.ok(coverRequestService.getPendingRequests());
    }

    // 📤 Get all requests submitted by a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ShiftCoverResponseDto>> getAllUserCoverRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(coverRequestService.getAllUserCoverRequests(userId));
    }

    @GetMapping("cover-request/${id}")
    public ResponseEntity<ShiftCoverResponseDto> getCoverRequest(@PathVariable Long id) {
            return ResponseEntity.ok(coverRequestService.getCoverRequestById(id));
    }


    // ⚠️ Check warnings before submitting (frontend pre-check)
    @PostMapping("/check")
    public ResponseEntity<List<String>> checkCoverEligibility(@RequestBody CoverEligibilityCheckDto dto) {
        return ResponseEntity.ok(coverRequestService.getApprovalWarnings(dto));
    }

    // ⚠️ Check warnings for a submitted request (admin view)
    @GetMapping("/{id}/warnings")
    public ResponseEntity<List<String>> getRequestWarnings(@PathVariable Long id) {
        return ResponseEntity.ok(coverRequestService.getApprovalWarnings(id));
    }

    // 🔄 Cancel a request (by user)
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelRequest(@PathVariable Long id) {
        coverRequestService.cancelRequest(id);
        return ResponseEntity.ok().build();
    }

    // ✅ Approve a request (admin action)
    @PutMapping("/{id}/approve")
    public ResponseEntity<Void> approveRequest(@PathVariable Long id) {
        coverRequestService.approveRequest(id);
        return ResponseEntity.ok().build();
    }

    // ❌ Reject a request (admin action)
    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> rejectRequest(@PathVariable Long id) {
        coverRequestService.rejectRequest(id);
        return ResponseEntity.ok().build();
    }
}
