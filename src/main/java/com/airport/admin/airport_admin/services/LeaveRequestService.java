package com.airport.admin.airport_admin.services;

import com.airport.admin.airport_admin.dto.LeaveRequestDto;
import com.airport.admin.airport_admin.enums.LeaveStatus;
import com.airport.admin.airport_admin.models.LeaveRequest;
import com.airport.admin.airport_admin.models.User;
import com.airport.admin.airport_admin.repositories.LeaveRequestRepository;
import com.airport.admin.airport_admin.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private UserRepository userRepository;

    // Apply for a new leave
    public LeaveRequest applyLeave(Long userId, LeaveRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LeaveRequest leave = new LeaveRequest();
        leave.setUser(user);
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setReason(dto.getReason());
        leave.setStatus(LeaveStatus.PENDING);

        return leaveRequestRepository.save(leave);
    }

    // Get leaves for logged-in user
    public List<LeaveRequest> getLeavesByUser(Long userId) {
        return leaveRequestRepository.findByUserId(userId);
    }

    // Get all leave requests (admin)
    public List<LeaveRequest> getAllLeaves() {
        return leaveRequestRepository.findAll();
    }

    // Approve or reject leave (admin)
    public LeaveRequest updateLeaveStatus(Long leaveId, LeaveStatus status) {
        LeaveRequest leave = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        leave.setStatus(status);
        return leaveRequestRepository.save(leave);
    }

    // Cancel leave (user)
    public LeaveRequest cancelLeave(Long leaveId, Long userId) {
        LeaveRequest leave = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        if (!leave.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to cancel this leave");
        }

        if (leave.getStatus() != LeaveStatus.PENDING && leave.getStatus() != LeaveStatus.RESUBMITTED) {
            throw new RuntimeException("Only pending or resubmitted leaves can be cancelled");
        }

        leave.setStatus(LeaveStatus.CANCELLED);
        return leaveRequestRepository.save(leave);
    }

    // Resubmit leave (user)
    public LeaveRequest resubmitLeave(Long leaveId, LeaveRequestDto dto, Long userId) {
        LeaveRequest leave = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

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

        return leaveRequestRepository.save(leave);
    }

    // Get a leave by ID (optional utility)
    public LeaveRequest getLeaveById(Long leaveId) {
        return leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
    }
}

