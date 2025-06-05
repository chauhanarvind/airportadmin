package com.airport.admin.airport_admin.features.Admin.jobRole;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobRoleRepository extends JpaRepository<JobRole,Long> {
    Optional<JobRole> findByRoleName(String jobRoleName);
}
