package com.airport.admin.airport_admin.features.jobLevel;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobLevelService {
    private final JobLevelRepository jobLevelRepository;
    private JobLevelService(JobLevelRepository jobLevelRepository){
        this.jobLevelRepository = jobLevelRepository;
    }

    public List<JobLevel> getAllJobLevels(){
        return jobLevelRepository.findAll();
    }

    public JobLevel createJobLevel(JobLevelDto jobLevelDto){
        if(jobLevelRepository.findByLevelName(jobLevelDto.getLevelName()).isPresent()){
            throw new RuntimeException("Job level with this name already exists");
        }

        JobLevel jobLevel = new JobLevel();
        jobLevel.setLevelName(jobLevelDto.getLevelName());

        return jobLevelRepository.save(jobLevel);
    }

    public JobLevel updateJobLevel(JobLevelDto jobLevelDto){
        JobLevel jobLevel = jobLevelRepository.findById(jobLevelDto.getId())
                .orElseThrow(()-> new RuntimeException("Job level does not exist"));

        jobLevel.setLevelName(jobLevelDto.getLevelName());
        return jobLevelRepository.save(jobLevel);
    }

    public void deleteJobLevel(Long id){
        JobLevel jobLevel = jobLevelRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Job level does not exist"));

        jobLevelRepository.delete(jobLevel);
    }
}
