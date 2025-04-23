package com.airport.admin.airport_admin.services;

import com.airport.admin.airport_admin.dto.JobCategoryDto;
import com.airport.admin.airport_admin.models.JobCategory;
import com.airport.admin.airport_admin.models.JobLevel;
import com.airport.admin.airport_admin.repositories.JobCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobCategoryService {
    private final JobCategoryRepository jobCategoryRepository;

    private JobCategoryService(JobCategoryRepository jobCategoryRepository){
        this.jobCategoryRepository = jobCategoryRepository;
    }

    public List<JobCategory> getAllJobCategory(){
        return jobCategoryRepository.findAll();
    }

    public JobCategory createJobCategory(JobCategoryDto jobCategoryDto){
        if(jobCategoryRepository.findByJobCategoryName(jobCategoryDto.getCategoryName()).isPresent()){
            throw new RuntimeException("Job Category with this name already exists");
        }

        JobCategory jobCategory = new JobCategory();
        jobCategory.setCategoryName(jobCategory.getCategoryName());
        return jobCategoryRepository.save(jobCategory);
    }

    public JobCategory updateJobCategory(JobCategoryDto jobCategoryDto){
        JobCategory jobCategory = jobCategoryRepository.findById(jobCategoryDto.getId())
                .orElseThrow(()-> new RuntimeException("Job category does not exist"));

        jobCategory.setCategoryName(jobCategory.getCategoryName());

        return jobCategoryRepository.save(jobCategory);
    }

    public void deleteJobCategory(JobCategoryDto jobCategoryDto){
        JobCategory jobCategory = jobCategoryRepository.findById(jobCategoryDto.getId())
                .orElseThrow(()-> new RuntimeException("Job category does not exist"));

        jobCategoryRepository.delete(jobCategory);
    }


}
