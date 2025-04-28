package com.airport.admin.airport_admin.controllers;

import com.airport.admin.airport_admin.dto.JobCategoryDto;
import com.airport.admin.airport_admin.models.JobCategory;
import com.airport.admin.airport_admin.services.JobCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-categories")
public class JobCategoryController {
    private final JobCategoryService jobCategoryService;

    private JobCategoryController(JobCategoryService jobCategoryService){
        this.jobCategoryService = jobCategoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<JobCategory> createJobCategory(@Valid @RequestBody JobCategoryDto jobCategoryDto){
        JobCategory jobCategory = jobCategoryService.createJobCategory(jobCategoryDto);
        return  ResponseEntity.ok(jobCategory);
    }

    @GetMapping("/")
    public ResponseEntity<List<JobCategory>> getJobCategories(){
        return ResponseEntity.ok(jobCategoryService.getAllJobCategory());
    }

    @PostMapping("/{id}")
    public ResponseEntity<JobCategory> updateJobCategory(@Valid @RequestBody JobCategoryDto jobCategoryDto){
        JobCategory jobCategory = jobCategoryService.updateJobCategory(jobCategoryDto);
        return  ResponseEntity.ok(jobCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<JobCategory> deleteJobCategory(@PathVariable Long id){
        jobCategoryService.deleteJobCategory(id);
        return  ResponseEntity.noContent().build();
    }
}
