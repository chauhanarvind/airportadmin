package com.airport.admin.airport_admin.features.shiftCover;

import com.airport.admin.airport_admin.features.shiftCover.dto.ShiftCoverRequestDto;
import com.airport.admin.airport_admin.features.shiftCover.dto.ShiftCoverResponseDto;
import com.airport.admin.airport_admin.features.shiftCover.dto.UserSummaryDto;
import com.airport.admin.airport_admin.features.user.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShiftCoverRequestMapper {

    // Request DTO (for creating new requests)
    public ShiftCoverRequest toEntity(ShiftCoverRequestDto dto) {
        ShiftCoverRequest entity = new ShiftCoverRequest();
        entity.setShiftDate(dto.getShiftDate());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setStatus(dto.getStatus());

        // Do not set user objects here – those are handled in the service
        return entity;
    }

    public ShiftCoverRequestDto toDto(ShiftCoverRequest entity) {
        ShiftCoverRequestDto dto = new ShiftCoverRequestDto();
        dto.setId(entity.getId());
        dto.setOriginalUserId(entity.getOriginalUser().getId());
        dto.setCoveringUserId(entity.getCoveringUser().getId());
        dto.setShiftDate(entity.getShiftDate());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    // Response DTO (for viewing requests)
    public ShiftCoverResponseDto toResponseDto(ShiftCoverRequest entity) {
        ShiftCoverResponseDto dto = new ShiftCoverResponseDto();
        dto.setId(entity.getId());
        dto.setShiftDate(entity.getShiftDate());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setStatus(entity.getStatus());
        dto.setOriginalUser(toUserSummary(entity.getOriginalUser()));
        dto.setCoveringUser(toUserSummary(entity.getCoveringUser()));
        return dto;
    }

    public List<ShiftCoverResponseDto> toResponseDtoList(List<ShiftCoverRequest> list) {
        List<ShiftCoverResponseDto> result = new ArrayList<>();
        for (ShiftCoverRequest req : list) {
            result.add(toResponseDto(req));
        }
        return result;
    }

    public List<ShiftCoverRequestDto> toDtoList(List<ShiftCoverRequest> list) {
        List<ShiftCoverRequestDto> result = new ArrayList<>();
        for (ShiftCoverRequest req : list) {
            result.add(toDto(req));
        }
        return result;
    }

    private UserSummaryDto toUserSummary(User user) {
        if (user == null) return null;

        UserSummaryDto summary = new UserSummaryDto();
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
