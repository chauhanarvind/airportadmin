package com.airport.admin.airport_admin.features.staff.roster;

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
        dto.setUnassigned(assignment.isUnassigned());

        if (assignment.getUser() != null) {
            dto.setUserFullName(assignment.getUser().getFirstName() + " " + assignment.getUser().getLastName());
        } else {
            dto.setUserFullName("Unassigned");
        }

        if (assignment.getRole() != null) {
            dto.setRoleName(assignment.getRole().getRoleName());
        }

        if (assignment.getLocation() != null) {
            dto.setLocationName(assignment.getLocation().getLocationName());
        }

        return dto;
    }

    public List<RosterAssignmentDto> mapToDtoList(List<RosterAssignment> assignments) {
        return assignments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
