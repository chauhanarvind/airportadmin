package com.airport.admin.airport_admin.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
public class StaffingRequestDayDto {

    @NotBlank(message = "Date is required")
    private String date;

    @NotEmpty(message = "At least one item is required")
    private List<StaffingRequestItemDto> items;
}
