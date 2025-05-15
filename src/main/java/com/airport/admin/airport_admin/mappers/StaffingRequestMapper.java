package com.airport.admin.airport_admin.mappers;

import com.airport.admin.airport_admin.dto.*;
import com.airport.admin.airport_admin.enums.LeaveStatus;
import com.airport.admin.airport_admin.enums.RequestType;
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

    public StaffingRequest mapDtoToEntity(StaffingRequestsDto dto) {
        // Lookup manager and location
        User manager = userRepository.findById(dto.getManagerId())
                .orElseThrow(() -> new RuntimeException("Manager not found"));
        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));

        StaffingRequest request = new StaffingRequest();
        request.setManager(manager);
        request.setLocation(location);
        request.setRequestType(RequestType.valueOf(dto.getRequestType().toUpperCase()));
        request.setReason(dto.getReason());
        request.setStatus(LeaveStatus.PENDING); // Default status

        List<StaffingRequestDay> dayEntities = new ArrayList<>();
        for (StaffingRequestDayDto dayDto : dto.getDays()) {
            StaffingRequestDay day = new StaffingRequestDay();
            day.setRequest(request);
            day.setDate(dayDto.getDate()); // Assumes yyyy-MM-dd

            List<StaffingRequestItem> itemEntities = new ArrayList<>();
            for (StaffingRequestItemDto itemDto : dayDto.getItems()) {
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
}
