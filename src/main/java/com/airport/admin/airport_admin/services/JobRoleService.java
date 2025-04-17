package com.airport.admin.airport_admin.services;

import com.airport.admin.airport_admin.models.JobRole;
import com.airport.admin.airport_admin.repositories.JobRoleRepository;
import com.airport.admin.airport_admin.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobRoleService {
    private final JobRoleRepository jobRoleRepository;

    private JobRoleService(JobRoleRepository jobRoleRepository){
    this.jobRoleRepository = jobRoleRepository;
    }

    public List<JobRole> getAllJobRoles(){
        return jobRoleRepository.findAll();
    }
}
