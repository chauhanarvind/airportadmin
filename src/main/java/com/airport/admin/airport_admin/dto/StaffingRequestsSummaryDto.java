package com.airport.admin.airport_admin.dto;

import com.airport.admin.airport_admin.enums.RequestType;
import com.airport.admin.airport_admin.enums.RosterStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;


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
