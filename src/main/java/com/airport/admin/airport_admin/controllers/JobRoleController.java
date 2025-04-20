package com.airport.admin.airport_admin.controllers;

import com.airport.admin.airport_admin.models.JobRole;
import com.airport.admin.airport_admin.services.JobRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/job-roles")
public class JobRoleController {
    private final JobRoleService jobRoleService;

    private JobRoleController(JobRoleService jobRoleService){
        this.jobRoleService = jobRoleService;
    }

    @GetMapping("/")
    public ResponseEntity<List<JobRole>> getAllJobRoles(){
        return ResponseEntity.ok(jobRoleService.getAllJobRoles());
    }
}
