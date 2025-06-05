package com.airport.admin.airport_admin.features.Admin.roles;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface    RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(String name);
}