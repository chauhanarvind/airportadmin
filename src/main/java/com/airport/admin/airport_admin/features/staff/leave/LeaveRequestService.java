package com.airport.admin.airport_admin.features.staff.leave;

import com.airport.admin.airport_admin.enums.LeaveStatus;
import com.airport.admin.airport_admin.features.Admin.user.User;
import com.airport.admin.airport_admin.features.Admin.user.UserRepository;
import com.airport.admin.airport_admin.features.staff.leave.dto.LeaveRequestCreateDto;
import com.airport.admin.airport_admin.features.staff.leave.dto.LeaveRequestGetDto;
import com.airport.admin.airport_admin.utils.AvailabilityConflictException;
import com.airport.admin.airport_admin.utils.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;

    public LeaveRequestService(
            LeaveRequestRepository leaveRequestRepository,
            UserRepository userRepository
    ) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.userRepository = userRepository;
    }

    // Authenticated user applies for leave
    public LeaveRequestGetDto applyLeave(Long userId, LeaveRequestCreateDto dto) {
        User user = getUserOrThrow(userId);
        LeaveRequest leave = LeaveRequestMapper.toEntity(dto, user);
        leave.setStatus(LeaveStatus.PENDING);
        leave = leaveRequestRepository.save(leave);
        return LeaveRequestMapper.toDto(leave);
    }

    public List<LeaveRequestGetDto> getLeavesByUser(Long userId) {
        List<LeaveRequest> leaves = leaveRequestRepository.findByUserId(userId);
        return LeaveRequestMapper.toDtoList(leaves);
    }

    public LeaveRequestGetDto getLeaveByLeaveId(Long id) {
        LeaveRequest leave = getLeaveOrThrow(id);
        return LeaveRequestMapper.toDto(leave);
    }

    public Page<LeaveRequestGetDto> getFilteredLeaves(Long userId, LeaveStatus status, Pageable pageable) {
        Specification<LeaveRequest> spec = LeaveRequestSpecification.build(userId, status);
        return leaveRequestRepository.findAll(spec, pageable)
                .map(LeaveRequestMapper::toDto);
    }

    public LeaveRequestGetDto updateLeaveStatus(Long leaveId, LeaveStatus status) {
        LeaveRequest leave = getLeaveOrThrow(leaveId);
        leave.setStatus(status);
        leave = leaveRequestRepository.save(leave);
        return LeaveRequestMapper.toDto(leave);
    }

    public LeaveRequestGetDto cancelLeave(Long leaveId) {
        LeaveRequest leave = getLeaveOrThrow(leaveId);

        if (!(leave.getStatus() == LeaveStatus.PENDING || leave.getStatus() == LeaveStatus.RESUBMITTED)) {
            throw new AvailabilityConflictException("Only pending or resubmitted leaves can be cancelled");
        }

        leave.setStatus(LeaveStatus.CANCELLED);
        leave = leaveRequestRepository.save(leave);
        return LeaveRequestMapper.toDto(leave);
    }

    public LeaveRequestGetDto resubmitLeave(Long userId, Long leaveId, LeaveRequestCreateDto dto) {
        LeaveRequest leave = getLeaveOrThrow(leaveId);

        if (!(leave.getStatus() == LeaveStatus.REJECTED || leave.getStatus() == LeaveStatus.CANCELLED)) {
            throw new AvailabilityConflictException("Only rejected or cancelled leaves can be resubmitted");
        }

        // Optional safety check: ensure user owns the leave being resubmitted
        if (!leave.getUser().getId().equals(userId)) {
            throw new AvailabilityConflictException("You cannot resubmit someone else's leave request");
        }

        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setReason(dto.getReason());
        leave.setStatus(LeaveStatus.RESUBMITTED);

        leave = leaveRequestRepository.save(leave);
        return LeaveRequestMapper.toDto(leave);
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private LeaveRequest getLeaveOrThrow(Long id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));
    }
}
