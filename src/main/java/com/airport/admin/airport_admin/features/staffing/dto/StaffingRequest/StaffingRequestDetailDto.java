package com.airport.admin.airport_admin.features.staffing.dto.StaffingRequest;

import com.airport.admin.airport_admin.enums.RequestType;
import com.airport.admin.airport_admin.enums.RosterStatus;
import com.airport.admin.airport_admin.features.staffing.dto.StaffingRequestDay.StaffingRequestDayDetailDto;
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
    private int unassignedShiftCount;

    private List<StaffingRequestDayDetailDto> days;
}
