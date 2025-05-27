package com.airport.admin.airport_admin.features.staffing.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class StaffingRequestDayDetailDto {
    private Long id;
    private LocalDate date;
    private List<StaffingRequestItemDetailDto> items;
}