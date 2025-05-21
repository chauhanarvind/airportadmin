package com.airport.admin.airport_admin.controllers;

import com.airport.admin.airport_admin.dto.JobRoleDto;
import com.airport.admin.airport_admin.dto.UserRequestDto;
import com.airport.admin.airport_admin.models.JobRole;
import com.airport.admin.airport_admin.models.User;
import com.airport.admin.airport_admin.services.JobRoleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-roles")
@PreAuthorize("hasRole('Admin')")
public class JobRoleController {
    private final JobRoleService jobRoleService;

    public JobRoleController(JobRoleService jobRoleService){
        this.jobRoleService = jobRoleService;
    }

    @GetMapping("/")
    public ResponseEntity<List<JobRole>> getAllJobRoles(){
        return ResponseEntity.ok(jobRoleService.getAllJobRoles());
    }

    @PostMapping("/create")
    public ResponseEntity<JobRole> createJobRole(@Valid @RequestBody JobRoleDto jobRoleDto) {
        JobRole role = jobRoleService.createJobRole(jobRoleDto);
        return ResponseEntity.ok(role);
    }


    @PostMapping("/{id}")
    public ResponseEntity<JobRole> updateJobRole(@Valid @RequestBody JobRoleDto jobRoleDto) {
        JobRole role = jobRoleService.updateJobRole(jobRoleDto);
        return ResponseEntity.ok(role);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<JobRole> deleteUser(@PathVariable Long id){
        jobRoleService.deleteJobRole(id);
        return  ResponseEntity.noContent().build();
    }
}
