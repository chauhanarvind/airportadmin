package com.airport.admin.airport_admin.features.staffing.dto.StaffingRequestItem;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class StaffingRequestItemCreateDto {

    @NotNull
    private Long jobRoleId;

    @NotNull
    private Long jobLevelId;

    @NotNull
    private Integer requiredCount;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;


}
