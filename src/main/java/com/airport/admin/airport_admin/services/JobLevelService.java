package com.airport.admin.airport_admin.services;

import com.airport.admin.airport_admin.models.JobLevel;
import com.airport.admin.airport_admin.repositories.JobLevelRepository;
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


}
