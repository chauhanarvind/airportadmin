package com.airport.admin.airport_admin.features.jobLevel;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-levels")
@PreAuthorize("hasRole('Admin')")
public class JobLevelController {
    private final JobLevelService jobLevelService;

    public JobLevelController(JobLevelService jobLevelService){
        this.jobLevelService = jobLevelService;
    }

    @GetMapping("/")
    public ResponseEntity<List<JobLevel>> getAllJobLevels(){
        return ResponseEntity.ok(jobLevelService.getAllJobLevels());
    }

    @PostMapping("/create")
    public ResponseEntity<JobLevel> createJobLevel(@Valid @RequestBody JobLevelDto jobLevelDto){
        JobLevel jobLevel = jobLevelService.createJobLevel(jobLevelDto);
        return ResponseEntity.ok(jobLevel);
    }


    @PostMapping("/{id}")
    public ResponseEntity<JobLevel> updateJobLevel(@Valid @RequestBody JobLevelDto jobLevelDto){
        JobLevel jobLevel = jobLevelService.updateJobLevel(jobLevelDto);
        return ResponseEntity.ok(jobLevel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<JobLevel> deleteJobLevel(@PathVariable Long id){
        jobLevelService.deleteJobLevel(id);
        return ResponseEntity.noContent().build();
    }
}
