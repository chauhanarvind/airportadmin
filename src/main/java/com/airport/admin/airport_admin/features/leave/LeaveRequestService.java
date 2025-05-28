package com.airport.admin.airport_admin.features.leave;

import com.airport.admin.airport_admin.enums.LeaveStatus;
import com.airport.admin.airport_admin.features.leave.dto.*;
import com.airport.admin.airport_admin.features.user.User;
import com.airport.admin.airport_admin.features.user.UserRepository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository, UserRepository userRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.userRepository = userRepository;
    }

    // 1. Apply for leave
    public LeaveRequestGetDto applyLeave(LeaveRequestCreateDto dto) {
        User user = getUserOrThrow(dto.getUserId());
        LeaveRequest leave = LeaveRequestMapper.toEntity(dto, user);
        leave.setStatus(LeaveStatus.PENDING);
        leave = leaveRequestRepository.save(leave);
        return LeaveRequestMapper.toDto(leave);
    }

    // 2. Get all leaves for a user
    public List<LeaveRequestGetDto> getLeavesByUser(Long userId) {
        List<LeaveRequest> leaves = leaveRequestRepository.findByUserId(userId);
        return LeaveRequestMapper.toDtoList(leaves);
    }

    //get leave by leave id
    public LeaveRequestGetDto getLeaveByLeaveId(Long id) {
        LeaveRequest leave = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        return LeaveRequestMapper.toDto(leave);
    }


//    // 3. Admin: Paginated leave requests (no filter)
//    public Page<LeaveRequestGetDto> getAllLeavesPaged(Pageable pageable) {
//        return leaveRequestRepository.findAll(pageable)
//                .map(LeaveRequestMapper::toDto);
//    }

    // 4. Admin: Filtered by user/status
    public Page<LeaveRequestGetDto> getFilteredLeaves(Long userId, LeaveStatus status, Pageable pageable) {
        Specification<LeaveRequest> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (userId != null) predicates.add(cb.equal(root.get("user").get("id"), userId));
            if (status != null) predicates.add(cb.equal(root.get("status"), status));
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return leaveRequestRepository.findAll(spec, pageable)
                .map(LeaveRequestMapper::toDto);
    }

    // 5. Admin: Update leave status
    public LeaveRequestGetDto updateLeaveStatus(Long leaveId, LeaveStatus status) {
        LeaveRequest leave = getLeaveOrThrow(leaveId);
        leave.setStatus(status);
        leave = leaveRequestRepository.save(leave);
        return LeaveRequestMapper.toDto(leave);
    }

    // 6. User: Cancel leave
    public LeaveRequestGetDto cancelLeave(Long leaveId, Long userId) {
        LeaveRequest leave = getLeaveOrThrow(leaveId);
        if (!leave.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to cancel this leave");
        }
        if (leave.getStatus() != LeaveStatus.PENDING && leave.getStatus() != LeaveStatus.RESUBMITTED) {
            throw new RuntimeException("Only pending or resubmitted leaves can be cancelled");
        }

        leave.setStatus(LeaveStatus.CANCELLED);
        leave = leaveRequestRepository.save(leave);
        return LeaveRequestMapper.toDto(leave);
    }

    // 7. User: Resubmit leave
    public LeaveRequestGetDto resubmitLeave(Long leaveId, LeaveRequestCreateDto dto, Long userId) {
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
        leave = leaveRequestRepository.save(leave);

        return LeaveRequestMapper.toDto(leave);
    }



    // 8. Utility methods
    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private LeaveRequest getLeaveOrThrow(Long id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
    }
}
