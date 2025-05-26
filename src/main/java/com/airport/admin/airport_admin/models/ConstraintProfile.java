package com.airport.admin.airport_admin.models;

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

    private String name; // e.g., "Weekday Morning Shifts"

    private Integer maxHoursPerDay;

    private Integer maxHoursPerWeek;

    private LocalTime preferredStartTime;

    private LocalTime preferredEndTime;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "constraint_profile_days", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "day")
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> allowedDays;
}
