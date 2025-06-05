package com.airport.admin.airport_admin.features.Admin.constraintProfile;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "constraint_profiles")
@Getter
@Setter
@NoArgsConstructor
public class ConstraintProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "max_hours_per_day")
    private Integer maxHoursPerDay;

    @Column(name = "max_hours_per_week")
    private Integer maxHoursPerWeek;

    @Column(name = "preferred_start_time")
    private LocalTime preferredStartTime;

    @Column(name = "preferred_end_time")
    private LocalTime preferredEndTime;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "constraint_profile_days",
            joinColumns = @JoinColumn(name = "profile_id")
    )
    @Column(name = "day")
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> allowedDays;
}
