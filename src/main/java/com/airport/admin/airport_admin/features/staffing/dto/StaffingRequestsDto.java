package com.airport.admin.airport_admin.features.staffing.dto;

import com.airport.admin.airport_admin.enums.RequestType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StaffingRequestsDto {

    @NotNull(message = "Manager id is required")
    private Long managerId;

    @NotNull(message = "Location id is required")
    private Long locationId;

    @NotBlank(message = "Request type is required")
    private RequestType requestType;

    private String reason;

    @NotEmpty(message = "At least one day is required")
    private List<StaffingRequestDayDto> days;
}
