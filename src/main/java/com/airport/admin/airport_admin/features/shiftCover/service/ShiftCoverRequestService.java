package com.airport.admin.airport_admin.features.shiftCover.service;

import com.airport.admin.airport_admin.enums.CoverRequestStatus;
import com.airport.admin.airport_admin.features.roster.RosterAssignmentRepository;
import com.airport.admin.airport_admin.features.shiftCover.ShiftCoverRequest;
import com.airport.admin.airport_admin.features.shiftCover.ShiftCoverRequestMapper;
import com.airport.admin.airport_admin.features.shiftCover.ShiftCoverRequestRepository;
import com.airport.admin.airport_admin.features.shiftCover.dto.CoverEligibilityCheckDto;
import com.airport.admin.airport_admin.features.shiftCover.dto.ShiftCoverRequestDto;
import com.airport.admin.airport_admin.features.shiftCover.dto.ShiftCoverResponseDto;
import com.airport.admin.airport_admin.features.user.User;
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

    // 📥 Submit new shift cover request
    public ShiftCoverResponseDto submitCoverRequest(ShiftCoverRequestDto dto) {
        User originalUser = userRepo.findById(dto.getOriginalUserId())
                .orElseThrow(() -> new RuntimeException("Original user not found"));
        User coveringUser = userRepo.findById(dto.getCoveringUserId())
                .orElseThrow(() -> new RuntimeException("Covering user not found"));

        ShiftCoverRequest request = mapper.toEntity(dto);
        request.setOriginalUser(originalUser);
        request.setCoveringUser(coveringUser);
        request.setStatus(CoverRequestStatus.PENDING);

        ShiftCoverRequest saved = requestRepo.save(request);
        return mapper.toResponseDto(saved);
    }

    // 📤 Get all cover requests
    public List<ShiftCoverResponseDto> getAllRequests() {
        return mapper.toResponseDtoList(requestRepo.findAll());
    }

    // 📤 Get only pending cover requests
    public List<ShiftCoverResponseDto> getPendingRequests() {
        return mapper.toResponseDtoList(requestRepo.findByStatus(CoverRequestStatus.PENDING));
    }

    // 📤 Get all requests for a user
    public List<ShiftCoverResponseDto> getAllUserCoverRequests(Long userId) {
        List<ShiftCoverRequest> requests = requestRepo.findByOriginalUserId(userId);
        return mapper.toResponseDtoList(requests);
    }

    public ShiftCoverResponseDto getCoverRequestById(Long id) {
        ShiftCoverRequest request = requestRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Request cover not found"));
        return mapper.toResponseDto(request);
    }

    // ⚠️ Check warnings before submitting (via DTO)
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

    // ⚠️ Check warnings for an existing request (admin view)
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

    // ✅ Approve a request and reassign shift
    public void approveRequest(Long requestId) {
        ShiftCoverRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!request.getStatus().equals(CoverRequestStatus.PENDING)) {
            throw new IllegalStateException("Only pending requests can be approved.");
        }

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

    // ❌ Reject a request
    public void rejectRequest(Long requestId) {
        ShiftCoverRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(CoverRequestStatus.REJECTED);
        requestRepo.save(request);
    }

    // 🔄 Cancel a request
    public void cancelRequest(Long requestId) {
        ShiftCoverRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!request.getStatus().equals(CoverRequestStatus.PENDING)) {
            throw new IllegalStateException("Only pending requests can be cancelled.");
        }

        request.setStatus(CoverRequestStatus.CANCELLED);
        requestRepo.save(request);
    }


}
