package com.airport.admin.airport_admin.mappers;

import com.airport.admin.airport_admin.dto.LeaveRequestDto;
import com.airport.admin.airport_admin.models.LeaveRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LeaveRequestMapper {
    public LeaveRequestDto toDto(LeaveRequest leave) {
        LeaveRequestDto dto = new LeaveRequestDto();
        dto.setId(leave.getId());
        dto.setStartDate(leave.getStartDate());
        dto.setEndDate(leave.getEndDate());
        dto.setReason(leave.getReason());
        dto.setStatus(leave.getStatus());
        dto.setCreatedAt(leave.getCreatedAt());

        dto.setUserId(leave.getUser().getId());
        dto.setUserFullName(leave.getUser().getFirstName() + " " + leave.getUser().getLastName());

        return dto;
    }

    public List<LeaveRequestDto> mapToDtoList(List<LeaveRequest> list) {
        return list.stream().map(this::toDto).toList();
    }

}
