package com.airport.admin.airport_admin.features.staff.staffing.dto.StaffingRequestItem;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class StaffingRequestItemResponseDto {
    private Long id;
    private String jobRoleName;
    private String jobLevelName;
    private Integer requiredCount;
    private LocalTime startTime;
    private LocalTime endTime;
}
