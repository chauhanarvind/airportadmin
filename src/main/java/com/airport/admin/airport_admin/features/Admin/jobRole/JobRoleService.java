package com.airport.admin.airport_admin.features.Admin.jobRole;

import com.airport.admin.airport_admin.features.Admin.jobCategory.JobCategory;
import com.airport.admin.airport_admin.features.Admin.jobCategory.JobCategoryRepository;
import com.airport.admin.airport_admin.features.Admin.jobRole.dto.JobRoleRequestDto;
import com.airport.admin.airport_admin.features.Admin.jobRole.dto.JobRoleResponseDto;
import com.airport.admin.airport_admin.utils.DuplicateResourceException;
import com.airport.admin.airport_admin.utils.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobRoleService {

    private final JobRoleRepository jobRoleRepository;
    private final JobCategoryRepository jobCategoryRepository;

    public JobRoleService(JobRoleRepository jobRoleRepository,
                          JobCategoryRepository jobCategoryRepository) {
        this.jobRoleRepository = jobRoleRepository;
        this.jobCategoryRepository = jobCategoryRepository;
    }

    public List<JobRoleResponseDto> getAllJobRoles() {
        return JobRoleMapper.toDtoList(jobRoleRepository.findAll());
    }

    public JobRoleResponseDto createJobRole(JobRoleRequestDto dto) {
        Optional<JobRole> existing = jobRoleRepository.findByRoleName(dto.getRoleName());
        if (existing.isPresent()) {
            throw new DuplicateResourceException("Job role with this name already exists");
        }

        JobCategory category = jobCategoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Job category does not exist"));

        JobRole jobRole = JobRoleMapper.toEntity(dto, category);
        JobRole saved = jobRoleRepository.save(jobRole);

        return JobRoleMapper.toDto(saved);
    }

    public JobRoleResponseDto updateJobRole(Long id, JobRoleRequestDto dto) {
        JobRole existing = jobRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job role does not exist"));

        JobCategory category = jobCategoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Job category does not exist"));

        JobRoleMapper.updateEntity(existing, dto, category);
        JobRole updated = jobRoleRepository.save(existing);

        return JobRoleMapper.toDto(updated);
    }

    public void deleteJobRole(Long id) {
        JobRole jobRole = jobRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job role does not exist"));

        jobRoleRepository.delete(jobRole);
    }

    public JobRoleResponseDto getJobRoleById(Long id) {
        JobRole jobRole = jobRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job role does not exist"));

        return JobRoleMapper.toDto(jobRole);
    }
}
