package com.airport.admin.airport_admin.features.staff.shiftCover.service;

import com.airport.admin.airport_admin.enums.CoverRequestStatus;
import com.airport.admin.airport_admin.features.Admin.user.User;
import com.airport.admin.airport_admin.features.Admin.user.UserRepository;
import com.airport.admin.airport_admin.features.staff.roster.RosterAssignmentRepository;
import com.airport.admin.airport_admin.features.staff.shiftCover.ShiftCoverRequest;
import com.airport.admin.airport_admin.features.staff.shiftCover.ShiftCoverRequestMapper;
import com.airport.admin.airport_admin.features.staff.shiftCover.ShiftCoverRequestRepository;
import com.airport.admin.airport_admin.features.staff.shiftCover.ShiftCoverRequestSpecification;
import com.airport.admin.airport_admin.features.staff.shiftCover.dto.CoverEligibilityCheckDto;
import com.airport.admin.airport_admin.features.staff.shiftCover.dto.ShiftCoverRequestCreateDto;
import com.airport.admin.airport_admin.features.staff.shiftCover.dto.ShiftCoverResponseDto;
import com.airport.admin.airport_admin.utils.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ShiftCoverRequestService {

    private final ShiftCoverRequestRepository requestRepo;
    private final UserRepository userRepo;
    private final RosterAssignmentRepository rosterRepo;
    private final ShiftCoverRequestMapper mapper;
    private final CoverEligibilityService coverEligibilityService;

    public ShiftCoverResponseDto submitCoverRequest(Long originalUserId, ShiftCoverRequestCreateDto dto) {
        User originalUser = userRepo.findById(originalUserId)
                .orElseThrow(() -> new RuntimeException("Original user not found"));

        User coveringUser = userRepo.findById(dto.getCoveringUserId())
                .orElseThrow(() -> new RuntimeException("Covering user not found"));

        System.out.println("covering user=="+coveringUser);


        System.out.println("shift ==" + dto.getShiftId());
        var shift = rosterRepo.findById(dto.getShiftId())
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        if (!originalUser.getId().equals(shift.getUser().getId())) {
            throw new IllegalStateException("You are not assigned to this shift.");
        }

        boolean exists = requestRepo.existsByShiftIdAndStatusIn(
                dto.getShiftId(),
                List.of(CoverRequestStatus.PENDING, CoverRequestStatus.APPROVED)
        );

        if (exists) {
            throw new DuplicateResourceException("A cover request already exists for this shift.");
        }

        ShiftCoverRequest request = mapper.toEntity(dto);
        request.setOriginalUser(originalUser);
        request.setCoveringUser(coveringUser);
        request.setShift(shift); // optional if you track the full shift entity
        request.setStatus(CoverRequestStatus.PENDING);

// ✅ These are required for DB insert
        request.setShiftDate(shift.getDate());
        request.setStartTime(shift.getStartTime());
        request.setEndTime(shift.getEndTime());

        return mapper.toResponseDto(requestRepo.save(request));
    }

    public Page<ShiftCoverResponseDto> getFilteredRequests(
            Long originalUserId,
            Long coveringUserId,
            CoverRequestStatus status,
            LocalDate shiftDate,
            Pageable pageable
    ) {
        Specification<ShiftCoverRequest> spec = ShiftCoverRequestSpecification.build(
                originalUserId, coveringUserId, status, shiftDate
        );
        return requestRepo.findAll(spec, pageable).map(mapper::toResponseDto);
    }

    public List<ShiftCoverResponseDto> getPendingRequests() {
        return mapper.toResponseDtoList(requestRepo.findByStatus(CoverRequestStatus.PENDING));
    }

    public List<ShiftCoverResponseDto> getAllUserCoverRequests(Long userId) {
        return mapper.toResponseDtoList(requestRepo.findByOriginalUserId(userId));
    }

    public ShiftCoverResponseDto getCoverRequestById(Long id) {
        ShiftCoverRequest request = requestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        return mapper.toResponseDto(request);
    }

    public List<String> getApprovalWarnings(CoverEligibilityCheckDto dto, Long originalUserId) {
        User coveringUser = userRepo.findById(dto.getCoveringUserId())
                .orElseThrow(() -> new RuntimeException("Covering user not found"));

        User originalUser = userRepo.findById(originalUserId)
                .orElseThrow(() -> new RuntimeException("Original user not found"));

        var shift = rosterRepo.findById(dto.getShiftId())
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        return coverEligibilityService.checkWarnings(coveringUser, originalUser, shift);
    }

    public List<String> getApprovalWarnings(Long requestId) {
        ShiftCoverRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        return coverEligibilityService.checkWarnings(
                request.getCoveringUser(),
                request.getOriginalUser(),
                request.getShift()
        );
    }

    public void approveRequest(Long requestId) {
        ShiftCoverRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus().equals(CoverRequestStatus.CANCELLED)) {
            throw new IllegalStateException("Cancelled requests can't be updated");
        }

        rosterRepo.updateUserForShift(
                request.getOriginalUser().getId(),
                request.getShift().getDate(),
                request.getShift().getStartTime(),
                request.getShift().getEndTime(),
                request.getCoveringUser().getId()
        );

        request.setStatus(CoverRequestStatus.APPROVED);
        requestRepo.save(request);
    }

    public void rejectRequest(Long requestId) {
        ShiftCoverRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(CoverRequestStatus.REJECTED);
        requestRepo.save(request);
    }

    public void resubmitRequest(Long requestId) {
        ShiftCoverRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() == CoverRequestStatus.REJECTED
                || request.getStatus() == CoverRequestStatus.CANCELLED
                || request.getStatus() == CoverRequestStatus.RESUBMITTED) {

            request.setStatus(CoverRequestStatus.RESUBMITTED);
            requestRepo.save(request);
        } else {
            throw new IllegalStateException("Only rejected, cancelled, or resubmitted requests can be resubmitted");
        }

    }


    public void cancelRequest(Long requestId) {
        ShiftCoverRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus().equals(CoverRequestStatus.PENDING)
                || request.getStatus().equals(CoverRequestStatus.RESUBMITTED)) {
            request.setStatus(CoverRequestStatus.CANCELLED);
            requestRepo.save(request);
        }else{
            throw new IllegalStateException("Only pending or resubmitted requests can be cancelled.");
        }
    }

    public ShiftCoverResponseDto getByUserAndShift(Long userId, Long shiftId) {
        ShiftCoverRequest request = requestRepo.findByOriginalUserIdAndShiftId(userId, shiftId)
                .orElse(null); // could also throw if needed
        return request != null ? mapper.toResponseDto(request) : null;
    }


}
