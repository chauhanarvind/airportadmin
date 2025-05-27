package com.airport.admin.airport_admin.features.staffing.repository;

import com.airport.admin.airport_admin.features.staffing.model.StaffingRequestItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffingRequestItemRepository extends JpaRepository<StaffingRequestItem, Long> {
}
