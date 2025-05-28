package com.airport.admin.airport_admin.features.jobLevel;

import com.airport.admin.airport_admin.features.jobLevel.dto.JobLevelRequestDto;
import com.airport.admin.airport_admin.features.jobLevel.dto.JobLevelResponseDto;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobLevelService {

    private final JobLevelRepository jobLevelRepository;

    public JobLevelService(JobLevelRepository jobLevelRepository) {
        this.jobLevelRepository = jobLevelRepository;
    }

    public List<JobLevelResponseDto> getAllJobLevels() {
        List<JobLevel> jobLevels = jobLevelRepository.findAll();
        return JobLevelMapper.toDtoList(jobLevels);
    }

    public JobLevelResponseDto getJobLevelById(Long id) {
        JobLevel jobLevel = jobLevelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job level does not exist"));
        return JobLevelMapper.toDto(jobLevel);
    }

    public JobLevelResponseDto createJobLevel(JobLevelRequestDto dto) {
        Optional<JobLevel> existing = jobLevelRepository.findByLevelName(dto.getLevelName());
        if (existing.isPresent()) {
            throw new RuntimeException("Job level with this name already exists");
        }

        JobLevel jobLevel = JobLevelMapper.toEntity(dto);
        jobLevel = jobLevelRepository.save(jobLevel);
        return JobLevelMapper.toDto(jobLevel);
    }

    public JobLevelResponseDto updateJobLevel(Long id, JobLevelRequestDto dto) {
        JobLevel existing = jobLevelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job level does not exist"));

        JobLevelMapper.updateEntity(existing, dto);
        existing = jobLevelRepository.save(existing);
        return JobLevelMapper.toDto(existing);
    }

    public void deleteJobLevel(Long id) {
        JobLevel jobLevel = jobLevelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job level does not exist"));
        jobLevelRepository.delete(jobLevel);
    }
}
