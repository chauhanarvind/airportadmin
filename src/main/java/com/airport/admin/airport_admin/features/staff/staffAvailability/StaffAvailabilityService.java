package com.airport.admin.airport_admin.features.staff.staffAvailability;


import com.airport.admin.airport_admin.utils.AvailabilityConflictException;
import com.airport.admin.airport_admin.utils.InvalidShiftException;
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

    public StaffAvailabilityResponseDto updateAvailability(Long id,Long userId, StaffAvailabilityRequestDto dto) {
        StaffAvailability existing = availabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Availability not found"));

        // Prevent duplicate entry for same user and date
        boolean exists = availabilityRepository.existsByUserIdAndDate(userId, dto.getDate());
        if (exists) {
            throw new AvailabilityConflictException("Availability already exists for this date.");
        }
        existing.setDate(dto.getDate());
        existing.setAvailable(dto.getIsAvailable());
        existing.setUnavailableFrom(dto.getUnavailableFrom());
        existing.setUnavailableTo(dto.getUnavailableTo());

        StaffAvailability updated = availabilityRepository.save(existing);
        return StaffAvailabilityMapper.toDto(updated);
    }



    public StaffAvailabilityResponseDto createAvailability(Long userId, StaffAvailabilityRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Prevent duplicate entry for same user and date
        boolean exists = availabilityRepository.existsByUserIdAndDate(userId, dto.getDate());
        if (exists) {
            throw new AvailabilityConflictException("Availability already exists for this date.");
        }

        StaffAvailability entity = new StaffAvailability();
        entity.setUser(user);
        entity.setDate(dto.getDate());
        entity.setAvailable(dto.getIsAvailable());
        entity.setUnavailableFrom(dto.getUnavailableFrom());
        entity.setUnavailableTo(dto.getUnavailableTo());

        StaffAvailability saved = availabilityRepository.save(entity);
        return StaffAvailabilityMapper.toDto(saved);
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
