package com.airport.admin.airport_admin.features.constraintProfile;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConstraintProfileService {

    private final ConstraintProfileRepository constraintProfileRepository;

    public ConstraintProfileService(ConstraintProfileRepository constraintProfileRepository) {
        this.constraintProfileRepository = constraintProfileRepository;
    }

    public List<ConstraintProfile> getAllProfiles() {
        return constraintProfileRepository.findAll();
    }

    public ConstraintProfile createProfile(ConstraintProfileDto dto) {
        ConstraintProfile profile = new ConstraintProfile();
        mapDtoToEntity(dto, profile);
        return constraintProfileRepository.save(profile);
    }

    public ConstraintProfile updateProfile(Long id, ConstraintProfileDto dto) {
        ConstraintProfile profile = constraintProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Constraint profile not found"));
        mapDtoToEntity(dto, profile);
        return constraintProfileRepository.save(profile);
    }

    public void deleteProfile(Long id) {
        constraintProfileRepository.deleteById(id);
    }

    public Optional<ConstraintProfile> getById(Long id) {
        return constraintProfileRepository.findById(id);
    }

    private void mapDtoToEntity(ConstraintProfileDto dto, ConstraintProfile entity) {
        entity.setName(dto.getName());
        entity.setMaxHoursPerDay(dto.getMaxHoursPerDay());
        entity.setMaxHoursPerWeek(dto.getMaxHoursPerWeek());
        entity.setAllowedDays(dto.getAllowedDays());
        entity.setPreferredStartTime(dto.getPreferredStartTime());
        entity.setPreferredEndTime(dto.getPreferredEndTime());
    }
}
