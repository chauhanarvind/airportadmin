package com.airport.admin.airport_admin.controllers;

import com.airport.admin.airport_admin.models.JobLevel;
import com.airport.admin.airport_admin.services.JobLevelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
