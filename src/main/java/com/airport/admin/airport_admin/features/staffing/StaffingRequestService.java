package com.airport.admin.airport_admin.features.staffing;

import com.airport.admin.airport_admin.ResourceNotFoundException;
import com.airport.admin.airport_admin.enums.RosterStatus;
import com.airport.admin.airport_admin.features.staffing.dto.StaffingRequest.*;
import com.airport.admin.airport_admin.features.staffing.model.StaffingRequest;
import com.airport.admin.airport_admin.features.staffing.repository.StaffingRequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaffingRequestService {

    @Autowired
    private StaffingRequestRepository staffingRequestRepository;

    @Autowired
    private StaffingRequestMapper staffingRequestMapper;

    // 1. Submit a new staffing request
    // transactional to reverse the query if unsuccessful
    @Transactional
    public StaffingRequestResponseDto submitRequest(StaffingRequestCreateDto dto) {
        StaffingRequest request = staffingRequestMapper.mapCreateDtoToEntity(dto);
        StaffingRequest saved = staffingRequestRepository.save(request);
        return staffingRequestMapper.toResponseDto(saved);
    }

    // 2. Get paged staffing requests (summary list)
    public Page<StaffingRequestResponseDto> getRequestsPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<StaffingRequest> requests = staffingRequestRepository.findAll(pageable);
        return requests.map(staffingRequestMapper::toResponseDto);
    }

    // 3. Get a specific request by ID (with full detail)
    public StaffingRequestDetailDto getRequestById(Long id) {
        StaffingRequest request = staffingRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staffing request not found with id: " + id));
        return staffingRequestMapper.toDetailDto(request);
    }

    // 4. Update the status of a request
    @Transactional
    public void updateStatus(Long requestId, RosterStatus status) {
        StaffingRequest request = staffingRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Staffing request not found with id: " + requestId));
        request.setStatus(status);
        staffingRequestRepository.save(request);
    }

    // 5. Get filtered requests (for admin dashboard)
    public Page<StaffingRequestResponseDto> getFilteredRequests(
            Optional<Long> managerId,
            Optional<Long> locationId,
            Optional<RosterStatus> status,
            Pageable pageable
    ) {
        Specification<StaffingRequest> spec = StaffingRequestSpecification.build(
                managerId.orElse(null),
                locationId.orElse(null),
                status.orElse(null)
        );

        Page<StaffingRequest> result = staffingRequestRepository.findAll(spec, pageable);
        return result.map(staffingRequestMapper::toResponseDto);
    }

    public List<StaffingRequestResponseDto> getRequestsByManagerId(Long managerId) {
        List<StaffingRequest> requests = staffingRequestRepository.findByManagerId(managerId);
        return requests.stream()
                .map(staffingRequestMapper::toResponseDto)
                .collect(Collectors.toList());
    }

}
