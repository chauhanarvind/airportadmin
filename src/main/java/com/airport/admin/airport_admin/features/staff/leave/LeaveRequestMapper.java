package com.airport.admin.airport_admin.features.staff.leave;

import com.airport.admin.airport_admin.features.Admin.user.User;
import com.airport.admin.airport_admin.features.staff.leave.dto.LeaveRequestCreateDto;
import com.airport.admin.airport_admin.features.staff.leave.dto.LeaveRequestGetDto;

import java.util.List;
import java.util.stream.Collectors;

public class LeaveRequestMapper {

    public static LeaveRequest toEntity(LeaveRequestCreateDto dto, User user) {
        LeaveRequest entity = new LeaveRequest();
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setReason(dto.getReason());
        entity.setUser(user);
        return entity;
    }

    public static LeaveRequestGetDto toDto(LeaveRequest entity) {
        LeaveRequestGetDto dto = new LeaveRequestGetDto();
        dto.setId(entity.getId());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setStatus(entity.getStatus());
        dto.setUserId(entity.getUser().getId());
        dto.setUserName(entity.getUser().getFirstName() + " " + entity.getUser().getLastName()); // or .getName() if available
        dto.setReason(entity.getReason());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public static List<LeaveRequestGetDto> toDtoList(List<LeaveRequest> entities) {
        return entities.stream()
                .map(LeaveRequestMapper::toDto)
                .collect(Collectors.toList());
    }
}
