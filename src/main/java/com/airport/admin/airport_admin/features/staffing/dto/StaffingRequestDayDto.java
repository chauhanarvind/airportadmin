package com.airport.admin.airport_admin.features.staffing.dto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
public class StaffingRequestDayDto {

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotEmpty(message = "At least one item is required")
    private List<StaffingRequestItemDto> items;
}
