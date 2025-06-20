package com.airport.admin.airport_admin.features.Admin.jobCategory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {
    Optional<JobCategory> findByCategoryName(String jobCategoryName);
}
