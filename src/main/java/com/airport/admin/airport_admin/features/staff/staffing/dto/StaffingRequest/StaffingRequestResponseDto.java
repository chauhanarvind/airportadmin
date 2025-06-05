package com.airport.admin.airport_admin.features.staff.staffing.dto.StaffingRequest;

import com.airport.admin.airport_admin.enums.RequestType;
import com.airport.admin.airport_admin.enums.RosterStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class StaffingRequestResponseDto {
    private Long id;

    private Long managerId;

    private String managerFirstName;

    private String managerLastName;

    private Long locationId;

    private String locationName;

    private String reason;

    private RequestType requestType;

    private OffsetDateTime createdAt;

    private RosterStatus status;
}
