package com.airport.admin.airport_admin.features.Admin.jobCategory;

import com.airport.admin.airport_admin.features.Admin.jobCategory.dto.JobCategoryRequestDto;
import com.airport.admin.airport_admin.features.Admin.jobCategory.dto.JobCategoryResponseDto;
import com.airport.admin.airport_admin.utils.DuplicateResourceException;
import com.airport.admin.airport_admin.utils.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobCategoryService {

    private final JobCategoryRepository jobCategoryRepository;

    public JobCategoryService(JobCategoryRepository jobCategoryRepository) {
        this.jobCategoryRepository = jobCategoryRepository;
    }

    public List<JobCategoryResponseDto> getAllJobCategories() {
        List<JobCategory> categories = jobCategoryRepository.findAll();
        return JobCategoryMapper.toDtoList(categories);
    }

    public JobCategoryResponseDto getJobCategoryById(Long id) {
        JobCategory category = jobCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job category does not exist"));
        return JobCategoryMapper.toDto(category);
    }

    public JobCategoryResponseDto createJobCategory(JobCategoryRequestDto dto) {
        Optional<JobCategory> existing = jobCategoryRepository.findByCategoryName(dto.getCategoryName());
        if (existing.isPresent()) {
            throw new DuplicateResourceException("Job category with this name already exists");
        }

        JobCategory category = JobCategoryMapper.toEntity(dto);
        category = jobCategoryRepository.save(category);
        return JobCategoryMapper.toDto(category);
    }

    public JobCategoryResponseDto updateJobCategory(Long id, JobCategoryRequestDto dto) {
        JobCategory existing = jobCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job category does not exist"));

        JobCategoryMapper.updateEntity(existing, dto);
        JobCategory updated = jobCategoryRepository.save(existing);

        return JobCategoryMapper.toDto(updated);
    }

    public void deleteJobCategory(Long id) {
        JobCategory category = jobCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job category does not exist"));
        jobCategoryRepository.delete(category);
    }
}
