package com.airport.admin.airport_admin.features.Admin.jobCategory;

import com.airport.admin.airport_admin.features.Admin.jobCategory.dto.JobCategoryRequestDto;
import com.airport.admin.airport_admin.features.Admin.jobCategory.dto.JobCategoryResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-categories")//can only be accessed by admin
public class JobCategoryController {

    private final JobCategoryService jobCategoryService;

    public JobCategoryController(JobCategoryService jobCategoryService) {
        this.jobCategoryService = jobCategoryService;
    }

    // to create
    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/create")
    public ResponseEntity<JobCategoryResponseDto> createJobCategory(
            @Valid @RequestBody JobCategoryRequestDto dto
    ) {
        return ResponseEntity.ok(jobCategoryService.createJobCategory(dto));
    }

    // to get all
    @GetMapping("/")
    public ResponseEntity<List<JobCategoryResponseDto>> getJobCategories() {
        return ResponseEntity.ok(jobCategoryService.getAllJobCategories());
    }

    // to get by id
    @GetMapping("/{id}")
    public ResponseEntity<JobCategoryResponseDto> getJobCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(jobCategoryService.getJobCategoryById(id));
    }

    // to update by id
    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/{id}")
    public ResponseEntity<JobCategoryResponseDto> updateJobCategory(
            @PathVariable Long id,
            @Valid @RequestBody JobCategoryRequestDto dto
    ) {
        return ResponseEntity.ok(jobCategoryService.updateJobCategory(id, dto));
    }

    // to delete by id
    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobCategory(@PathVariable Long id) {
        jobCategoryService.deleteJobCategory(id);
        return ResponseEntity.noContent().build();
    }
}
