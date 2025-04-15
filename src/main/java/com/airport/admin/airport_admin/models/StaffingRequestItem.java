package com.airport.admin.airport_admin.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private StaffingRequestDay day;

    @ManyToOne
    @JoinColumn(name = "job_role_id", nullable = false)
    private JobRole role;

    @ManyToOne
    @JoinColumn(name = "job_level_id", nullable = false)
    private JobLevel jobLevel;

    @Column(name = "required_count", nullable = false)
    private Integer requiredCount;

    @Column(name = "start_time", nullable = false)
    private String startTime;

    @Column(name = "end_time", nullable = false)
    private String endTime;
}
