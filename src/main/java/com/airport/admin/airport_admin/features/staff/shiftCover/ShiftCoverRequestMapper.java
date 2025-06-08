package com.airport.admin.airport_admin.features.staff.shiftCover;

import com.airport.admin.airport_admin.features.Admin.user.User;
import com.airport.admin.airport_admin.features.staff.roster.RosterAssignment;
import com.airport.admin.airport_admin.features.staff.shiftCover.dto.ShiftCoverRequestCreateDto;
import com.airport.admin.airport_admin.features.staff.shiftCover.dto.ShiftCoverResponseDto;
import com.airport.admin.airport_admin.features.staff.shiftCover.dto.UserSummaryDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShiftCoverRequestMapper {

    // Create empty entity (service sets user and shift)
    public ShiftCoverRequest toEntity(ShiftCoverRequestCreateDto dto) {
        // Don't set shift or users here — those are handled in the service
        return new ShiftCoverRequest();
    }

    // Convert to response DTO
    public ShiftCoverResponseDto toResponseDto(ShiftCoverRequest entity) {
        ShiftCoverResponseDto dto = new ShiftCoverResponseDto();

        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setOriginalUser(toUserSummary(entity.getOriginalUser()));
        dto.setCoveringUser(toUserSummary(entity.getCoveringUser()));

        RosterAssignment shift = entity.getShift();
        if (shift != null) {
            dto.setShiftId(shift.getId());
            dto.setShiftDate(shift.getDate());
            dto.setStartTime(shift.getStartTime());
            dto.setEndTime(shift.getEndTime());
        }

        return dto;
    }

    public List<ShiftCoverResponseDto> toResponseDtoList(List<ShiftCoverRequest> list) {
        List<ShiftCoverResponseDto> result = new ArrayList<>();
        for (ShiftCoverRequest req : list) {
            result.add(toResponseDto(req));
        }
        return result;
    }

    private UserSummaryDto toUserSummary(User user) {
        if (user == null) return null;

        UserSummaryDto summary = new UserSummaryDto();
        summary.setId(user.getId());
        summary.setFirstName(user.getFirstName());
        summary.setLastName(user.getLastName());
        summary.setEmail(user.getEmail());
        summary.setRoleName(user.getRole() != null ? user.getRole().getName() : null);
        summary.setJobRoleName(user.getJobRole() != null ? user.getJobRole().getRoleName() : null);
        summary.setJobLevelName(user.getJobLevel() != null ? user.getJobLevel().getLevelName() : null);
        summary.setConstraintProfileName(user.getConstraintProfile() != null ? user.getConstraintProfile().getName() : null);
        return summary;
    }
}
