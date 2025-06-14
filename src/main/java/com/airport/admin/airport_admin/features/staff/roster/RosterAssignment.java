package com.airport.admin.airport_admin.features.staff.roster;

import com.airport.admin.airport_admin.features.Admin.jobRole.JobRole;
import com.airport.admin.airport_admin.features.Admin.location.Location;
import com.airport.admin.airport_admin.features.staff.staffing.model.StaffingRequest;
import com.airport.admin.airport_admin.features.Admin.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "roster_assignments")
@Getter
@Setter
@NoArgsConstructor
public class RosterAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_role_id", nullable = false)
    private JobRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;


    private boolean unassigned;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private StaffingRequest request;

}
