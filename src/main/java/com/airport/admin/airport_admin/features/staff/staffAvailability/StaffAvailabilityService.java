package com.airport.admin.airport_admin.features.staff.staffAvailability;


import com.airport.admin.airport_admin.utils.AvailabilityConflictException;
import com.airport.admin.airport_admin.utils.ResourceNotFoundException;
import com.airport.admin.airport_admin.enums.LeaveStatus;
import com.airport.admin.airport_admin.features.staff.leave.LeaveRequestRepository;
import com.airport.admin.airport_admin.features.staff.staffAvailability.dto.StaffAvailabilityRequestDto;
import com.airport.admin.airport_admin.features.staff.staffAvailability.dto.StaffAvailabilityResponseDto;
import com.airport.admin.airport_admin.features.Admin.user.User;
import com.airport.admin.airport_admin.features.Admin.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class StaffAvailabilityService {

    private final StaffAvailabilityRepository availabilityRepository;
    private final UserRepository userRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    public StaffAvailabilityService(StaffAvailabilityRepository availabilityRepository,
                                    UserRepository userRepository,
                                    LeaveRequestRepository leaveRequestRepository) {
        this.availabilityRepository = availabilityRepository;
        this.userRepository = userRepository;
        this.leaveRequestRepository = leaveRequestRepository;
    }

    public StaffAvailabilityResponseDto saveAvailability(StaffAvailabilityRequestDto dto) {
        User user = getUserOrThrow(dto.getUserId());

        // Block if user is already on approved leave on this date
        boolean onLeave = leaveRequestRepository
                .existsByUserIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        dto.getUserId(),
                        LeaveStatus.APPROVED,
                        dto.getDate(),
                        dto.getDate()
                );


        if (onLeave) {
            throw new AvailabilityConflictException("User is on approved leave for this date and cannot set availability.");
        }

        // Validate time fields if available
        if (Boolean.TRUE.equals(dto.getIsAvailable())) {
            LocalTime from = dto.getUnavailableFrom();
            LocalTime to = dto.getUnavailableTo();

            if ((from == null) != (to == null)) {
                throw new AvailabilityConflictException("Both unavailableFrom and unavailableTo must be set if one is provided.");
            }

            if (from != null && from.isAfter(to)) {
                throw new AvailabilityConflictException("unavailableFrom must be before unavailableTo.");
            }
        }

        // Create or update availability
        StaffAvailability availability = availabilityRepository
                .findByUserIdAndDate(dto.getUserId(), dto.getDate())
                .orElseGet(StaffAvailability::new);

        StaffAvailabilityMapper.updateEntity(availability, dto, user);
        availability = availabilityRepository.save(availability);

        return StaffAvailabilityMapper.toDto(availability);
    }

    public List<StaffAvailabilityResponseDto> getAvailabilityByUser(Long userId) {
        List<StaffAvailability> entries = availabilityRepository.findByUserId(userId);
        return StaffAvailabilityMapper.toDtoList(entries);
    }

    public StaffAvailabilityResponseDto getById(Long id) {
        StaffAvailability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Availability entry not found"));
        return StaffAvailabilityMapper.toDto(availability);
    }

    public void deleteById(Long id) {
        if (!availabilityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Availability entry not found");
        }
        availabilityRepository.deleteById(id);
    }

    public Page<StaffAvailabilityResponseDto> getFilteredAvailability(Long userId, LocalDate date, Pageable pageable) {
        Specification<StaffAvailability> spec = StaffAvailabilitySpecification.build(userId, date);
        return availabilityRepository.findAll(spec, pageable)
                .map(StaffAvailabilityMapper::toDto);
    }


    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
