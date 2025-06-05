package com.airport.admin.airport_admin.features.Admin.jobLevel;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobLevelRepository extends JpaRepository<JobLevel, Long> {
    Optional<JobLevel> findByLevelName (String levelName);
}
