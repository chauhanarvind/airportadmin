package com.airport.admin.airport_admin.features.staffing.dto.StaffingRequestDay;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import com.airport.admin.airport_admin.features.staffing.dto.StaffingRequestItem.StaffingRequestItemCreateDto;

@Getter
@Setter
public class StaffingRequestDayCreateDto {

    @NotNull(message = "date is required")
    private LocalDate date;

    @NotEmpty(message = "at least one item is required")
    private List<@Valid StaffingRequestItemCreateDto> items;
}
