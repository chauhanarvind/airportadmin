package com.airport.admin.airport_admin.features.jobRole;

import com.airport.admin.airport_admin.features.jobRole.dto.JobRoleRequestDto;
import com.airport.admin.airport_admin.features.jobRole.dto.JobRoleResponseDto;
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

    public JobRoleController(JobRoleService jobRoleService) {
        this.jobRoleService = jobRoleService;
    }

    @GetMapping("/")
    public ResponseEntity<List<JobRoleResponseDto>> getAllJobRoles() {
        return ResponseEntity.ok(jobRoleService.getAllJobRoles());
    }

    @PostMapping("/create")
    public ResponseEntity<JobRoleResponseDto> createJobRole(@Valid @RequestBody JobRoleRequestDto dto) {
        return ResponseEntity.ok(jobRoleService.createJobRole(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobRoleResponseDto> updateJobRole(
            @PathVariable Long id,
            @Valid @RequestBody JobRoleRequestDto dto
    ) {
        return ResponseEntity.ok(jobRoleService.updateJobRole(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobRole(@PathVariable Long id) {
        jobRoleService.deleteJobRole(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobRoleResponseDto> getJobRoleById(@PathVariable Long id) {
        JobRoleResponseDto dto = jobRoleService.getJobRoleById(id);
        return ResponseEntity.ok(dto);
    }
}
