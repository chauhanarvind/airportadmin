package com.airport.admin.airport_admin.features.staffing.dto.StaffingRequestDay;

import com.airport.admin.airport_admin.features.staffing.dto.StaffingRequestItem.StaffingRequestItemResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class StaffingRequestDayDetailDto {
    private Long id;
    private LocalDate date;
    private List<StaffingRequestItemResponseDto> items;
}