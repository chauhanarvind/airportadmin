package com.airport.admin.airport_admin.features.staff.staffing.model;

import com.airport.admin.airport_admin.features.Admin.jobLevel.JobLevel;
import com.airport.admin.airport_admin.features.Admin.jobRole.JobRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "staffing_request_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StaffingRequestItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "day_id", nullable = false)
    @JsonIgnore
    private StaffingRequestDay day;

    @ManyToOne
    @JoinColumn(name = "job_role_id", nullable = false)
    private JobRole jobRole;

    @ManyToOne
    @JoinColumn(name = "job_level_id", nullable = false)
    private JobLevel jobLevel;

    @Column(name = "required_count", nullable = false)
    private Integer requiredCount;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
}
