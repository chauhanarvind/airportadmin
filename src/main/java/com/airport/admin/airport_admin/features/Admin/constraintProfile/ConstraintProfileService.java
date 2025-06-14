package com.airport.admin.airport_admin.features.Admin.constraintProfile;

import com.airport.admin.airport_admin.features.Admin.constraintProfile.dto.ConstraintProfileRequestDto;
import com.airport.admin.airport_admin.features.Admin.constraintProfile.dto.ConstraintProfileResponseDto;
import com.airport.admin.airport_admin.utils.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConstraintProfileService {

    private final ConstraintProfileRepository constraintProfileRepository;

    public ConstraintProfileService(ConstraintProfileRepository constraintProfileRepository) {
        this.constraintProfileRepository = constraintProfileRepository;
    }

    public List<ConstraintProfileResponseDto> getAllProfiles() {
        return ConstraintProfileMapper.toDtoList(constraintProfileRepository.findAll());
    }

    public ConstraintProfileResponseDto getProfileById(Long id) {
        ConstraintProfile profile = constraintProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Constraint profile not found"));
        return ConstraintProfileMapper.toDto(profile);
    }

    public ConstraintProfileResponseDto createProfile(ConstraintProfileRequestDto dto) {
        ConstraintProfile profile = ConstraintProfileMapper.toEntity(dto);
        profile = constraintProfileRepository.save(profile);
        return ConstraintProfileMapper.toDto(profile);
    }

    public ConstraintProfileResponseDto updateProfile(Long id, ConstraintProfileRequestDto dto) {
        ConstraintProfile existing = constraintProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Constraint profile not found"));

        ConstraintProfileMapper.updateEntity(existing, dto);
        existing = constraintProfileRepository.save(existing);

        return ConstraintProfileMapper.toDto(existing);
    }

    public void deleteProfile(Long id) {
        if (!constraintProfileRepository.existsById(id)) {
            throw new ResourceNotFoundException("Constraint profile not found");
        }
        constraintProfileRepository.deleteById(id);
    }
}
