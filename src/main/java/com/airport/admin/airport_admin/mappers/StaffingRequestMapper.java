package com.airport.admin.airport_admin.mappers;

import com.airport.admin.airport_admin.dto.*;
import com.airport.admin.airport_admin.enums.RequestType;
import com.airport.admin.airport_admin.enums.RosterStatus;
import com.airport.admin.airport_admin.models.*;
import com.airport.admin.airport_admin.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StaffingRequestMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private JobRoleRepository jobRoleRepository;

    @Autowired
    private JobLevelRepository jobLevelRepository;

    //for submit requests
    public StaffingRequest mapDtoToEntity(StaffingRequestsDto dto) {
        if (dto.getManagerId()==null){
           throw new IllegalArgumentException("Manager id is null");
        }else if(dto.getLocationId() == null){
            throw new IllegalArgumentException("Location id is null");
        }
        // Lookup manager and location
        User manager = userRepository.findById(dto.getManagerId())
                .orElseThrow(() -> new RuntimeException("Manager not found"));
        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));

        StaffingRequest request = new StaffingRequest();
        request.setManager(manager);
        request.setLocation(location);
        request.setRequestType(dto.getRequestType());
        request.setReason(dto.getReason());
        request.setStatus(RosterStatus.PENDING); // Default status

        List<StaffingRequestDay> dayEntities = new ArrayList<>();
        for (StaffingRequestDayDto dayDto : dto.getDays()) {
            StaffingRequestDay day = new StaffingRequestDay();
            day.setRequest(request);
            day.setDate(dayDto.getDate()); // Assumes yyyy-MM-dd

            List<StaffingRequestItem> itemEntities = new ArrayList<>();
            for (StaffingRequestItemDto itemDto : dayDto.getItems()) {
                if(itemDto.getJobLevelId() == null){
                    throw new IllegalArgumentException("Job level id is null");
                }else if(itemDto.getJobRoleId() == null){
                    throw new IllegalArgumentException("Job role is null");
                }
                StaffingRequestItem item = new StaffingRequestItem();
                item.setDay(day);
                item.setJobRole(jobRoleRepository.findById(itemDto.getJobRoleId())
                        .orElseThrow(() -> new RuntimeException("JobRole not found")));
                item.setJobLevel(jobLevelRepository.findById(itemDto.getJobLevelId())
                        .orElseThrow(() -> new RuntimeException("JobLevel not found")));
                item.setRequiredCount(itemDto.getRequiredCount());
                item.setStartTime(itemDto.getStartTime());
                item.setEndTime(itemDto.getEndTime());
                itemEntities.add(item);
            }

            day.setItems(itemEntities);
            dayEntities.add(day);
        }

        request.setDays(dayEntities);
        return request;
    }

    //for get requests
    public StaffingRequestsSummaryDto toSummaryDto(StaffingRequest request) {
        StaffingRequestsSummaryDto dto = new StaffingRequestsSummaryDto();
        dto.setId(request.getId());
        dto.setManagerId(request.getManager().getId());
        dto.setManagerFirstName(request.getManager().getFirstName());
        dto.setManagerLastName(request.getManager().getLastName());

        dto.setLocationId(request.getLocation().getId());
        dto.setLocationName(request.getLocation().getLocationName());

        dto.setRequestType(request.getRequestType());

        dto.setReason(request.getReason());

        dto.setCreatedAt(request.getCreatedAt());

        dto.setStatus(request.getStatus());

        return dto;
    }

    //for /id get request
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

            List<StaffingRequestItemDetailDto> itemDtos = day.getItems().stream().map(item -> {
                StaffingRequestItemDetailDto itemDto = new StaffingRequestItemDetailDto();
                itemDto.setId(item.getId());
                itemDto.setJobRoleName(item.getJobRole().getRoleName());
                itemDto.setJobLevelName(item.getJobLevel().getLevelName());
                itemDto.setRequiredCount(item.getRequiredCount());
                itemDto.setStartTime(item.getStartTime());
                itemDto.setEndTime(item.getEndTime());
                return itemDto;
            }).toList();

            dayDto.setItems(itemDtos);
            return dayDto;
        }).toList();

        dto.setDays(dayDtos);
        return dto;
    }



}
