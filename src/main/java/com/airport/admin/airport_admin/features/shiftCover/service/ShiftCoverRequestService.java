package com.airport.admin.airport_admin.features.shiftCover.service;

import com.airport.admin.airport_admin.features.shiftCover.ShiftCoverRequest;
import com.airport.admin.airport_admin.features.shiftCover.ShiftCoverRequestMapper;
import com.airport.admin.airport_admin.features.shiftCover.ShiftCoverRequestRepository;
import com.airport.admin.airport_admin.features.shiftCover.dto.CoverEligibilityCheckDto;
import com.airport.admin.airport_admin.enums.CoverRequestStatus;
import com.airport.admin.airport_admin.features.shiftCover.dto.ShiftCoverRequestDto;
import com.airport.admin.airport_admin.features.user.User;
import com.airport.admin.airport_admin.features.roster.RosterAssignmentRepository;
import com.airport.admin.airport_admin.features.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftCoverRequestService {

    private final ShiftCoverRequestRepository requestRepo;
    private final UserRepository userRepo;
    private final ShiftCoverRequestMapper mapper;
    private final CoverEligibilityService coverEligibilityService;
    private final RosterAssignmentRepository rosterRepo;

    // 📥 Submit new shift cover request (no constraint checks here)
    public ShiftCoverRequestDto submitCoverRequest(ShiftCoverRequestDto dto) {
        User originalUser = userRepo.findById(dto.getOriginalUserId())
                .orElseThrow(() -> new RuntimeException("Original user not found"));
        User coveringUser = userRepo.findById(dto.getCoveringUserId())
                .orElseThrow(() -> new RuntimeException("Covering user not found"));

        ShiftCoverRequest request = mapper.toEntity(dto);
        request.setOriginalUser(originalUser);
        request.setCoveringUser(coveringUser);
        request.setStatus(CoverRequestStatus.PENDING);

        ShiftCoverRequest saved = requestRepo.save(request);
        return mapper.toDto(saved);
    }

    // 📤 Get all cover requests
    public List<ShiftCoverRequestDto> getAllRequests() {
        return mapper.toDtoList(requestRepo.findAll());
    }

    // 📤 Get only pending requests
    public List<ShiftCoverRequestDto> getPendingRequests() {
        return mapper.toDtoList(requestRepo.findByStatus(CoverRequestStatus.PENDING));
    }

    // ⚠️ Check warnings before submission or approval (via DTO)
    public List<String> getApprovalWarnings(CoverEligibilityCheckDto dto) {
        User user = userRepo.findById(dto.getCoveringUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return coverEligibilityService.checkWarnings(
                user,
                dto.getShiftDate(),
                dto.getStartTime(),
                dto.getEndTime()
        );
    }

    // ⚠️ Check warnings for an existing request (admin review)
    public List<String> getApprovalWarnings(Long requestId) {
        ShiftCoverRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        return coverEligibilityService.checkWarnings(
                request.getCoveringUser(),
                request.getShiftDate(),
                request.getStartTime(),
                request.getEndTime()
        );
    }

    // ✅ Approve request (shift reassignment happens here)
    public void approveRequest(Long requestId) {
        ShiftCoverRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!request.getStatus().equals(CoverRequestStatus.PENDING)) {
            throw new IllegalStateException("Only pending requests can be approved.");
        }

        // 🔁 Reassign the shift to covering user
        rosterRepo.updateUserForShift(
                request.getOriginalUser().getId(),
                request.getShiftDate(),
                request.getStartTime(),
                request.getEndTime(),
                request.getCoveringUser().getId()
        );

        request.setStatus(CoverRequestStatus.APPROVED);
        requestRepo.save(request);
    }

    // ❌ Reject request
    public void rejectRequest(Long requestId) {
        ShiftCoverRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(CoverRequestStatus.REJECTED);
        requestRepo.save(request);
    }
}
