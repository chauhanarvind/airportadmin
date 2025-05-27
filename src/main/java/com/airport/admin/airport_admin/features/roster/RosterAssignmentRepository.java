package com.airport.admin.airport_admin.features.roster;

import com.airport.admin.airport_admin.features.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface RosterAssignmentRepository extends JpaRepository<RosterAssignment, Long> {

    // Find assignments by user and specific date
    List<RosterAssignment> findByUserIdAndDate(Long userId, LocalDate date);

    // Find assignments by user and date range
    List<RosterAssignment> findByUserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);

    // Delete by dates and location (used during auto-gen overwrite)
    void deleteByDateInAndLocationId(List<LocalDate> dates, Long locationId);

    // Fetch by date + location
    List<RosterAssignment> findByDateAndLocationId(LocalDate date, Long locationId);

    // Fetch by date range + location
    List<RosterAssignment> findByDateBetweenAndLocationId(LocalDate start, LocalDate end, Long locationId);

    // Ordered fetch (e.g. for viewing weekly roster)
    List<RosterAssignment> findByDateBetweenAndLocationIdOrderByDateAscStartTimeAsc(
            LocalDate start, LocalDate end, Long locationId
    );

    //  Check if user has overlapping shift on given date and time
    @Query("""
        SELECT COUNT(r) > 0 FROM RosterAssignment r
        WHERE r.user = :user AND r.date = :date
        AND (
            (:start < r.endTime AND :end > r.startTime)
        )
    """)
    boolean existsByUserAndShiftDateAndTimeOverlap(
            @Param("user") User user,
            @Param("date") LocalDate date,
            @Param("start") LocalTime start,
            @Param("end") LocalTime end
    );

    // Update user for a specific shift (requires native query if needed)
    @Modifying
    @Transactional
    @Query("""
        UPDATE RosterAssignment r
        SET r.user.id = :newUserId
        WHERE r.user.id = :oldUserId
        AND r.date = :date
        AND r.startTime = :startTime
        AND r.endTime = :endTime
    """)
    void updateUserForShift(
            @Param("oldUserId") Long oldUserId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("newUserId") Long newUserId
    );
}
