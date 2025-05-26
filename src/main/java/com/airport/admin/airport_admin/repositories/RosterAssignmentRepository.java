package com.airport.admin.airport_admin.repositories;

import com.airport.admin.airport_admin.models.RosterAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RosterAssignmentRepository extends JpaRepository<RosterAssignment, Long> {
    List<RosterAssignment> findByUserIdAndDate(Long userId, LocalDate date);

    List<RosterAssignment> findByUserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);

    void deleteByDateInAndLocationId(List<LocalDate> dates, Long locationId);

    List<RosterAssignment> findByDateAndLocationId(LocalDate date, Long locationId);
    List<RosterAssignment> findByDateBetweenAndLocationId(LocalDate start, LocalDate end, Long locationId);
    List<RosterAssignment> findByDateBetweenAndLocationIdOrderByDateAscStartTimeAsc(
            LocalDate start, LocalDate end, Long locationId
    );

}
