package com.airport.admin.airport_admin.mappers;

import com.airport.admin.airport_admin.dto.RosterAssignmentDto;
import com.airport.admin.airport_admin.models.RosterAssignment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RosterAssignmentMapper {
    public RosterAssignmentDto toDto(RosterAssignment assignment) {
        RosterAssignmentDto dto = new RosterAssignmentDto();

        dto.setId(assignment.getId());
        dto.setDate(assignment.getDate());
        dto.setStartTime(assignment.getStartTime());
        dto.setEndTime(assignment.getEndTime());

        dto.setUserFullName(
                assignment.getUser().getFirstName() + " " + assignment.getUser().getLastName()
        );
        dto.setRoleName(assignment.getRole().getRoleName());
        dto.setLocationName(assignment.getLocation().getLocationName());

        return dto;
    }

    public List<RosterAssignmentDto> mapToDtoList(List<RosterAssignment> assignments) {
        return assignments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


}
