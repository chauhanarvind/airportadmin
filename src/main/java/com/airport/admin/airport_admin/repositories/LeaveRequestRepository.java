package com.airport.admin.airport_admin.repositories;
import com.airport.admin.airport_admin.enums.LeaveStatus;
import com.airport.admin.airport_admin.models.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    // Get all leaves by user ID
    List<LeaveRequest> findByUserId(Long userId);

    // Get leaves by status
    List<LeaveRequest> findByStatus(LeaveStatus status);

    List<LeaveRequest> findByUserIdAndStatus(Long userId, LeaveStatus status);

}
