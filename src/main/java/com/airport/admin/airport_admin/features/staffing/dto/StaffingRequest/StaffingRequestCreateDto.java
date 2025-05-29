package com.airport.admin.airport_admin.features.staffing.dto.StaffingRequest;

import com.airport.admin.airport_admin.enums.RequestType;
import com.airport.admin.airport_admin.features.staffing.dto.StaffingRequestDay.StaffingRequestDayCreateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StaffingRequestCreateDto {

    @NotNull
    private Long managerId;

    @NotNull
    private Long locationId;

    private RequestType requestType = RequestType.REGULAR;

    private String reason;

    @Valid
    @NotEmpty(message = "At least one day is required")
    private List<StaffingRequestDayCreateDto> days;
}