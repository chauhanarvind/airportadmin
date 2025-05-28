package com.airport.admin.airport_admin.features.staffAvailability;

import com.airport.admin.airport_admin.enums.LeaveStatus;
import com.airport.admin.airport_admin.features.leave.LeaveRequestRepository;
import com.airport.admin.airport_admin.features.staffAvailability.dto.StaffAvailabilityRequestDto;
import com.airport.admin.airport_admin.features.staffAvailability.dto.StaffAvailabilityResponseDto;
import com.airport.admin.airport_admin.features.user.User;
import com.airport.admin.airport_admin.features.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffAvailabilityService {

    private final StaffAvailabilityRepository availabilityRepository;
    private final UserRepository userRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    public StaffAvailabilityService(StaffAvailabilityRepository availabilityRepository, UserRepository userRepository,
                                    LeaveRequestRepository leaveRequestRepository) {
        this.availabilityRepository = availabilityRepository;
        this.userRepository = userRepository;
        this.leaveRequestRepository = leaveRequestRepository;
    }

    // Create or update (based on user & date)
    public StaffAvailabilityResponseDto saveAvailability(StaffAvailabilityRequestDto dto) {
        User user = getUserOrThrow(dto.getUserId());

        // ❗ Block if user is already on approved leave on this date
        boolean onLeave = leaveRequestRepository
                .existsByUserAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        user,
                        LeaveStatus.APPROVED,
                        dto.getDate(),
                        dto.getDate()
                );

        if (onLeave) {
            throw new RuntimeException("User is on approved leave for this date and cannot set availability.");
        }

        StaffAvailability availability = availabilityRepository
                .findByUserIdAndDate(dto.getUserId(), dto.getDate())
                .orElseGet(StaffAvailability::new);

        StaffAvailabilityMapper.updateEntity(availability, dto, user);
        availability = availabilityRepository.save(availability);

        return StaffAvailabilityMapper.toDto(availability);
    }


    // Get all for user
    public List<StaffAvailabilityResponseDto> getAvailabilityByUser(Long userId) {
        List<StaffAvailability> entries = availabilityRepository.findByUserId(userId);
        return StaffAvailabilityMapper.toDtoList(entries);
    }

    // Get by ID
    public StaffAvailabilityResponseDto getById(Long id) {
        StaffAvailability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Availability entry not found"));
        return StaffAvailabilityMapper.toDto(availability);
    }

    // Delete by ID
    public void deleteById(Long id) {
        if (!availabilityRepository.existsById(id)) {
            throw new RuntimeException("Availability entry not found");
        }
        availabilityRepository.deleteById(id);
    }

    public List<StaffAvailabilityResponseDto> getAllAvailability() {
        List<StaffAvailability> records = availabilityRepository.findAll();
        return StaffAvailabilityMapper.toDtoList(records);
    }


    // Utility
    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
