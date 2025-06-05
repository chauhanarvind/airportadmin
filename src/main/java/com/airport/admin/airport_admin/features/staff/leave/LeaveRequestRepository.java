package com.airport.admin.airport_admin.features.staff.leave;
import com.airport.admin.airport_admin.enums.LeaveStatus;
import com.airport.admin.airport_admin.features.Admin.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long>,
        JpaSpecificationExecutor<LeaveRequest> {

    // Get all leaves by user ID
    List<LeaveRequest> findByUserId(Long userId);

    // Get leaves by status
    List<LeaveRequest> findByStatus(LeaveStatus status);

    // Get leaves by user and status
    List<LeaveRequest> findByUserIdAndStatus(Long userId, LeaveStatus status);

    // Check if user is on leave during a given date
    boolean existsByUserAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            User user,
            LeaveStatus status,
            LocalDate startDate,
            LocalDate endDate
    );

    boolean existsByUserIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long userId,
            LeaveStatus status,
            LocalDate startDate,
            LocalDate endDate
    );

}
