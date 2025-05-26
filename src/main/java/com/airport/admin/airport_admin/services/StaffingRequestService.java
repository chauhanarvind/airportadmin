package com.airport.admin.airport_admin.services;

import com.airport.admin.airport_admin.dto.StaffingRequestDetailDto;
import com.airport.admin.airport_admin.dto.StaffingRequestsDto;
import com.airport.admin.airport_admin.enums.RosterStatus;
import com.airport.admin.airport_admin.models.StaffingRequest;
import com.airport.admin.airport_admin.repositories.StaffingRequestRepository;
import com.airport.admin.airport_admin.mappers.StaffingRequestMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    // 2. Get paged requests
    public Page<StaffingRequest> getRequestsPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return staffingRequestRepository.findAll(pageable);
    }


    //  4. Get a specific request by ID
    public StaffingRequestDetailDto getRequestById(Long id) {
        StaffingRequest staffingRequest =  staffingRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + id));

        return staffingRequestMapper.toDetailDto(staffingRequest);
    }

    //  5. Update the status of a request
    @Transactional
    public void  updateStatus(Long requestId, RosterStatus status) {
        StaffingRequest request = staffingRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));
        request.setStatus(status);

        staffingRequestRepository.save(request);
    }

    // 6. get filtered requests
    public Page<StaffingRequest> getFilteredRequests(
            Optional<Long> managerId,
            Optional<Long> locationId,
            Optional<String> status,
            Pageable pageable
    ) {
        Specification<StaffingRequest> spec = StaffingRequestSpecification.build(
                managerId.orElse(null),
                locationId.orElse(null),
                status.orElse(null)
        );

        return staffingRequestRepository.findAll(spec, pageable);
    }
}
