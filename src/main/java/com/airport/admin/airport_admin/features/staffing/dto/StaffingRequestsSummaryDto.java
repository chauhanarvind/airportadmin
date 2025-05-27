package com.airport.admin.airport_admin.features.staffing.dto;

import com.airport.admin.airport_admin.enums.RequestType;
import com.airport.admin.airport_admin.enums.RosterStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;


//this for read-only(get requests)
@Getter
@Setter
public class StaffingRequestsSummaryDto {
    private Long id;

    private Long managerId;

    private String managerFirstName;

    private String managerLastName;

    private Long locationId;

    private String locationName;

    private String reason;

    private RequestType requestType;

    private OffsetDateTime createdAt;

    private RosterStatus Status;
}
