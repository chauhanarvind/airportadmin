package com.airport.admin.airport_admin.features.staffing;

import com.airport.admin.airport_admin.enums.RosterStatus;
import com.airport.admin.airport_admin.features.jobLevel.JobLevelRepository;
import com.airport.admin.airport_admin.features.jobRole.JobRoleRepository;
import com.airport.admin.airport_admin.features.location.*;
import com.airport.admin.airport_admin.features.staffing.dto.StaffingRequest.*;
import com.airport.admin.airport_admin.features.staffing.dto.StaffingRequestDay.*;
import com.airport.admin.airport_admin.features.staffing.dto.StaffingRequestItem.*;
import com.airport.admin.airport_admin.features.staffing.model.*;
import com.airport.admin.airport_admin.features.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StaffingRequestMapper {

    @Autowired private UserRepository userRepository;
    @Autowired private LocationRepository locationRepository;
    @Autowired private JobRoleRepository jobRoleRepository;
    @Autowired private JobLevelRepository jobLevelRepository;

    // 1. Convert Create DTO to Entity
    public StaffingRequest mapCreateDtoToEntity(StaffingRequestCreateDto dto) {
        User manager = userRepository.findById(dto.getManagerId())
                .orElseThrow(() -> new RuntimeException("Manager not found"));
        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));

        StaffingRequest request = new StaffingRequest();
        request.setManager(manager);
        request.setLocation(location);
        request.setRequestType(dto.getRequestType());
        request.setReason(dto.getReason());
        request.setStatus(RosterStatus.PENDING);

        List<StaffingRequestDay> dayEntities = new ArrayList<>();
        for (StaffingRequestDayCreateDto dayDto : dto.getDays()) {
            StaffingRequestDay day = new StaffingRequestDay();
            day.setRequest(request);
            day.setDate(dayDto.getDate());

            List<StaffingRequestItem> itemEntities = new ArrayList<>();
            for (StaffingRequestItemCreateDto itemDto : dayDto.getItems()) {
                StaffingRequestItem item = new StaffingRequestItem();
                item.setDay(day);
                item.setRequiredCount(itemDto.getRequiredCount());
                item.setStartTime(itemDto.getStartTime());
                item.setEndTime(itemDto.getEndTime());
                item.setJobRole(jobRoleRepository.findById(itemDto.getJobRoleId())
                        .orElseThrow(() -> new RuntimeException("JobRole not found")));
                item.setJobLevel(jobLevelRepository.findById(itemDto.getJobLevelId())
                        .orElseThrow(() -> new RuntimeException("JobLevel not found")));
                itemEntities.add(item);
            }

            day.setItems(itemEntities);
            dayEntities.add(day);
        }

        request.setDays(dayEntities);
        return request;
    }

    // 2. Convert Entity to Response DTO (list view)
    public StaffingRequestResponseDto toResponseDto(StaffingRequest request) {
        StaffingRequestResponseDto dto = new StaffingRequestResponseDto();
        dto.setId(request.getId());
        dto.setManagerId(request.getManager().getId());
        dto.setManagerFirstName(request.getManager().getFirstName());
        dto.setManagerLastName(request.getManager().getLastName());
        dto.setLocationId(request.getLocation().getId());
        dto.setLocationName(request.getLocation().getLocationName());
        dto.setReason(request.getReason());
        dto.setRequestType(request.getRequestType());
        dto.setStatus(request.getStatus());
        dto.setCreatedAt(request.getCreatedAt());
        return dto;
    }

    // 3. Convert Entity to Detail DTO (for /id)
    public StaffingRequestDetailDto toDetailDto(StaffingRequest request) {
        StaffingRequestDetailDto dto = new StaffingRequestDetailDto();
        dto.setId(request.getId());
        dto.setManagerId(request.getManager().getId());
        dto.setManagerFirstName(request.getManager().getFirstName());
        dto.setManagerLastName(request.getManager().getLastName());
        dto.setLocationId(request.getLocation().getId());
        dto.setLocationName(request.getLocation().getLocationName());
        dto.setReason(request.getReason());
        dto.setRequestType(request.getRequestType());
        dto.setStatus(request.getStatus());
        dto.setCreatedAt(request.getCreatedAt());

        List<StaffingRequestDayDetailDto> dayDtos = request.getDays().stream().map(day -> {
            StaffingRequestDayDetailDto dayDto = new StaffingRequestDayDetailDto();
            dayDto.setId(day.getId());
            dayDto.setDate(day.getDate());

            List<StaffingRequestItemResponseDto> itemDtos = day.getItems().stream().map(item -> {
                StaffingRequestItemResponseDto itemDto = new StaffingRequestItemResponseDto();
                itemDto.setId(item.getId());
                itemDto.setJobRoleName(item.getJobRole().getRoleName());
                itemDto.setJobLevelName(item.getJobLevel().getLevelName());
                itemDto.setRequiredCount(item.getRequiredCount());
                itemDto.setStartTime(item.getStartTime());
                itemDto.setEndTime(item.getEndTime());
                return itemDto;
            }).collect(Collectors.toList());

            dayDto.setItems(itemDtos);
            return dayDto;
        }).collect(Collectors.toList());

        dto.setDays(dayDtos);
        return dto;
    }
}
