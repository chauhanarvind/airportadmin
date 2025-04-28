package com.airport.admin.airport_admin.controllers;

import com.airport.admin.airport_admin.dto.JobLevelDto;
import com.airport.admin.airport_admin.dto.UserRequestDto;
import com.airport.admin.airport_admin.models.JobLevel;
import com.airport.admin.airport_admin.models.User;
import com.airport.admin.airport_admin.services.JobLevelService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-levels")
public class JobLevelController {
    private final JobLevelService jobLevelService;

    private JobLevelController(JobLevelService jobLevelService){
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
