
package com.airport.admin.airport_admin.features.Admin.constraintProfile;

import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class ConstraintProfileDto {

    private Long id;

    private String name;

    private Integer maxHoursPerDay;

    private Integer maxHoursPerWeek;

    private LocalTime preferredStartTime;

    private LocalTime preferredEndTime;

    private List<DayOfWeek> allowedDays;
}
