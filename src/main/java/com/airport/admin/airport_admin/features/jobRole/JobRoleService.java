package com.airport.admin.airport_admin.features.jobRole;

import com.airport.admin.airport_admin.features.jobCategory.JobCategory;
import com.airport.admin.airport_admin.features.jobCategory.JobCategoryRepository;
import com.airport.admin.airport_admin.features.jobRole.dto.JobRoleRequestDto;
import com.airport.admin.airport_admin.features.jobRole.dto.JobRoleResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Optional;

@Service
public class JobRoleService {

    private final JobRoleRepository jobRoleRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final JobRoleMapper jobRoleMapper;

    public JobRoleService(JobRoleRepository jobRoleRepository,
                          JobCategoryRepository jobCategoryRepository,
                          JobRoleMapper jobRoleMapper) {
        this.jobRoleRepository = jobRoleRepository;
        this.jobCategoryRepository = jobCategoryRepository;
        this.jobRoleMapper = jobRoleMapper;
    }

    public List<JobRoleResponseDto> getAllJobRoles() {
        return jobRoleMapper.toDtoList(jobRoleRepository.findAll());
    }

    public JobRoleResponseDto createJobRole(JobRoleRequestDto dto) {
        Optional<JobRole> existing = jobRoleRepository.findByRoleName(dto.getRoleName());
        if (existing.isPresent()) {
            throw new RuntimeException("Job role with this name already exists");
        }

        JobCategory category = jobCategoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Job category does not exist"));

        JobRole jobRole = jobRoleMapper.toEntity(dto, category);
        JobRole saved = jobRoleRepository.save(jobRole);

        return jobRoleMapper.toDto(saved);
    }

    public JobRoleResponseDto updateJobRole(Long id, JobRoleRequestDto dto) {
        JobRole existing = jobRoleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job role does not exist"));

        JobCategory category = jobCategoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Job category does not exist"));

        // Update the existing entity before saving
        jobRoleMapper.updateEntity(existing, dto, category);

        JobRole updated = jobRoleRepository.save(existing);
        return jobRoleMapper.toDto(updated);
    }

    public void deleteJobRole(Long id) {
        JobRole jobRole = jobRoleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job role does not exist"));

        jobRoleRepository.delete(jobRole);
    }

    public JobRoleResponseDto getJobRoleById(Long id) {
        JobRole jobRole = jobRoleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job role does not exist"));

        return jobRoleMapper.toDto(jobRole);
    }

}
