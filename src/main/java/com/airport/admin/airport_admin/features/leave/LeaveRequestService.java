package com.airport.admin.airport_admin.features.leave;

import com.airport.admin.airport_admin.enums.LeaveStatus;
import com.airport.admin.airport_admin.features.user.User;
import com.airport.admin.airport_admin.features.user.UserRepository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;
    @Autowired LeaveRequestMapper leaveRequestMapper;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository, UserRepository userRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.userRepository = userRepository;
    }

    // Apply for leave
    public LeaveRequestDto applyLeave(Long userId, LeaveRequestDto dto) {
        User user = getUserOrThrow(userId);

        LeaveRequest leave = new LeaveRequest();
        leave.setUser(user);
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setReason(dto.getReason());
        leave.setStatus(LeaveStatus.PENDING);

        LeaveRequest leaveRequest = leaveRequestRepository.save(leave);
        return leaveRequestMapper.toDto(leaveRequest);
    }

    // Get all leaves for a user
    public List<LeaveRequestDto> getLeavesByUser(Long userId) {
        List<LeaveRequest> leaveRequest = leaveRequestRepository.findByUserId(userId);
        return leaveRequestMapper.mapToDtoList(leaveRequest);
    }

    // Paginated admin view (no filters)
    public Page<LeaveRequestDto> getAllLeavesPaged(Pageable pageable) {
        Page<LeaveRequest> leaveRequestPage = leaveRequestRepository.findAll(pageable);
        return leaveRequestPage.map(leaveRequestMapper::toDto);
    }

    // Paginated + filtered (by user and/or status)
    public Page<LeaveRequestDto> getFilteredLeaves(Long userId, LeaveStatus status, Pageable pageable) {
        Specification<LeaveRequest> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userId != null) {
                predicates.add(cb.equal(root.get("user").get("id"), userId));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<LeaveRequest> leaveRequestPage = leaveRequestRepository.findAll(spec, pageable);
        return leaveRequestPage.map(leaveRequestMapper::toDto);
    }

    // Approve / Reject leave (Admin)
    public LeaveRequestDto updateLeaveStatus(Long leaveId, LeaveStatus status) {
        LeaveRequest leave = getLeaveOrThrow(leaveId);
        leave.setStatus(status);
        LeaveRequest leaveRequest = leaveRequestRepository.save(leave);
        return leaveRequestMapper.toDto(leaveRequest);
    }

    // Cancel leave (User)
    public LeaveRequestDto cancelLeave(Long leaveId, Long userId) {
        LeaveRequest leave = getLeaveOrThrow(leaveId);
        if (!leave.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to cancel this leave");
        }

        if (leave.getStatus() != LeaveStatus.PENDING && leave.getStatus() != LeaveStatus.RESUBMITTED) {
            throw new RuntimeException("Only pending or resubmitted leaves can be cancelled");
        }

        leave.setStatus(LeaveStatus.CANCELLED);
        LeaveRequest leaveRequest = leaveRequestRepository.save(leave);
        return leaveRequestMapper.toDto(leaveRequest);
    }

    // Resubmit leave (User)
    public LeaveRequestDto resubmitLeave(Long leaveId, LeaveRequestDto dto, Long userId) {
        LeaveRequest leave = getLeaveOrThrow(leaveId);
        if (!leave.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to resubmit this leave");
        }

        if (leave.getStatus() != LeaveStatus.REJECTED && leave.getStatus() != LeaveStatus.CANCELLED) {
            throw new RuntimeException("Only rejected or cancelled leaves can be resubmitted");
        }

        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setReason(dto.getReason());
        leave.setStatus(LeaveStatus.RESUBMITTED);

        LeaveRequest leaveRequest = leaveRequestRepository.save(leave);
        return leaveRequestMapper.toDto(leaveRequest);
    }

    // Utility methods
    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private LeaveRequest getLeaveOrThrow(Long id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
    }

}
