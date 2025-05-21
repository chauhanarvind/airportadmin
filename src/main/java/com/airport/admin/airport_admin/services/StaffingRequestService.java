package com.airport.admin.airport_admin.services;

import com.airport.admin.airport_admin.dto.StaffingRequestsDto;
import com.airport.admin.airport_admin.enums.LeaveStatus;
import com.airport.admin.airport_admin.models.StaffingRequest;
import com.airport.admin.airport_admin.repositories.StaffingRequestRepository;
import com.airport.admin.airport_admin.mappers.StaffingRequestMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffingRequestService {

    @Autowired
    private StaffingRequestRepository staffingRequestRepository;

    @Autowired
    private StaffingRequestMapper staffingRequestMapper;

    // 1. Save a new staffing request from DTO
    @Transactional
    public StaffingRequest submitRequest(StaffingRequestsDto dto) {
        StaffingRequest request = staffingRequestMapper.mapDtoToEntity(dto);
        return staffingRequestRepository.save(request);
    }

    // 2. Get all staffing requests (for admin)
    public List<StaffingRequest> getAllRequests() {
        return staffingRequestRepository.findAll();
    }

    //  3. Get all requests submitted by a specific manager
    public List<StaffingRequest> getRequestsByManager(Long managerId) {
        return staffingRequestRepository.findByManagerId(managerId);
    }

    //  4. Get a specific request by ID
    public StaffingRequest getRequestById(Long id) {
        return staffingRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + id));
    }

    //  5. Update the status of a request
    @Transactional
    public StaffingRequest updateStatus(Long requestId, LeaveStatus status) {
        StaffingRequest request = staffingRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));
        request.setStatus(status);
        return staffingRequestRepository.save(request);
    }
}
