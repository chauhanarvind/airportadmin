package com.airport.admin.airport_admin.services;

import com.airport.admin.airport_admin.dto.JobRoleDto;
import com.airport.admin.airport_admin.models.JobCategory;
import com.airport.admin.airport_admin.models.JobRole;
import com.airport.admin.airport_admin.repositories.JobCategoryRepository;
import com.airport.admin.airport_admin.repositories.JobRoleRepository;
import com.airport.admin.airport_admin.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobRoleService {
    private final JobRoleRepository jobRoleRepository;
    private final JobCategoryRepository jobCategoryRepository;

    private JobRoleService(JobRoleRepository jobRoleRepository, JobCategoryRepository jobCategoryRepository){
        this.jobRoleRepository = jobRoleRepository;
        this.jobCategoryRepository = jobCategoryRepository;
    }

    public List<JobRole> getAllJobRoles(){
        return jobRoleRepository.findAll();
    }

    public JobRole createJobRole(JobRoleDto jobRoleDto){
        if(jobRoleRepository.findByJobRoleName(jobRoleDto.getRoleName()).isPresent()){
            throw new RuntimeException("Job role with this name already exists");
        }

        JobCategory jobCategory = jobCategoryRepository.findById(jobRoleDto.getCategoryId())
                .orElseThrow(()-> new RuntimeException("The job category does not exist"));

        JobRole jobRole = new JobRole();
        jobRole.setRoleName(jobRoleDto.getRoleName());
        jobRole.setCategory(jobCategory);
        return jobRoleRepository.save(jobRole);
    }

    public JobRole updateJobRole(JobRoleDto jobRoleDto){
        JobRole jobRole = jobRoleRepository.findById(jobRoleDto.getId())
                .orElseThrow(()-> new RuntimeException("Job role does not exist"));

        JobCategory jobCategory = jobCategoryRepository.findById(jobRoleDto.getCategoryId())
                .orElseThrow(()-> new RuntimeException("The job category does not exist"));

        jobRole.setRoleName(jobRoleDto.getRoleName());
        jobRole.setCategory(jobCategory);

        return  jobRoleRepository.save(jobRole);
    }

    public void deleteJobRole(JobRoleDto jobRoleDto){
        JobRole jobRole = jobRoleRepository.findById(jobRoleDto.getId())
                .orElseThrow(()-> new RuntimeException("Job role does not exist"));

        jobRoleRepository.delete(jobRole);
    }
}
