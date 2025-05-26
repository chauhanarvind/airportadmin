package com.airport.admin.airport_admin.dto;

import com.airport.admin.airport_admin.enums.RequestType;
import com.airport.admin.airport_admin.enums.RosterStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class StaffingRequestDetailDto {
    private Long id;

    private Long managerId;
    private String managerFirstName;
    private String managerLastName;

    private Long locationId;
    private String locationName;

    private String reason;
    private RequestType requestType;
    private RosterStatus status;
    private OffsetDateTime createdAt;

    private List<StaffingRequestDayDetailDto> days;
}
