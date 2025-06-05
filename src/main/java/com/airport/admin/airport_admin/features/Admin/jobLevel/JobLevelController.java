package com.airport.admin.airport_admin.features.Admin.jobLevel;

import com.airport.admin.airport_admin.features.Admin.jobLevel.dto.JobLevelRequestDto;
import com.airport.admin.airport_admin.features.Admin.jobLevel.dto.JobLevelResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-levels")
@PreAuthorize("hasRole('Admin')") //can only be accessed by admin
public class JobLevelController {

    private final JobLevelService jobLevelService;

    public JobLevelController(JobLevelService jobLevelService) {
        this.jobLevelService = jobLevelService;
    }

    //get all
    @GetMapping("/")
    public ResponseEntity<List<JobLevelResponseDto>> getAllJobLevels() {
        return ResponseEntity.ok(jobLevelService.getAllJobLevels());
    }

    // get by id
    @GetMapping("/{id}")
    public ResponseEntity<JobLevelResponseDto> getJobLevelById(@PathVariable Long id) {
        return ResponseEntity.ok(jobLevelService.getJobLevelById(id));
    }

    // to create
    @PostMapping("/create")
    public ResponseEntity<JobLevelResponseDto> createJobLevel(
            @Valid @RequestBody JobLevelRequestDto dto
    ) {
        return ResponseEntity.ok(jobLevelService.createJobLevel(dto));
    }


    //to update by ud
    @PutMapping("/{id}")
    public ResponseEntity<JobLevelResponseDto> updateJobLevel(
            @PathVariable Long id,
            @Valid @RequestBody JobLevelRequestDto dto
    ) {
        return ResponseEntity.ok(jobLevelService.updateJobLevel(id, dto));
    }

    // to delete by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobLevel(@PathVariable Long id) {
        jobLevelService.deleteJobLevel(id);
        return ResponseEntity.noContent().build();
    }
}
