package com.airport.admin.airport_admin.services;

import com.airport.admin.airport_admin.dto.StaffingRequestsDto;
import com.airport.admin.airport_admin.enums.RequestStatus;
import com.airport.admin.airport_admin.enums.RequestType;
import com.airport.admin.airport_admin.models.*;
import com.airport.admin.airport_admin.repositories.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StaffingRequestService {
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final JobRoleRepository jobRoleRepository;
    private final JobLevelRepository jobLevelRepository;
    private final StaffingRequestRepository staffingRequestRepository;
    public StaffingRequestService(UserRepository userRepository, LocationRepository locationRepository,
                                  JobRoleRepository jobRoleRepository, JobLevelRepository jobLevelRepository,
                                  StaffingRequestRepository staffingRequestRepository){
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.jobRoleRepository = jobRoleRepository;
        this.jobLevelRepository = jobLevelRepository;
        this.staffingRequestRepository= staffingRequestRepository;
    }

    //create new staffing request service
    public StaffingRequest createNewStaffingRequest(StaffingRequestsDto dto){
        //validate manager and location
        User manager =userRepository.findById(dto.getManagerId())
                .orElseThrow(()-> new RuntimeException("Manager not found with id: " + dto.getManagerId()));

        Location location = locationRepository.findById(dto.getManagerId())
                .orElseThrow(()-> new RuntimeException("Location not found with id: " + dto.getLocationId()));

        //Map request data
        StaffingRequest request = new StaffingRequest();
        request.setManager(manager);
        request.setLocation(location);
        request.setRequestType(RequestType.valueOf(dto.getRequestType()));

        request.setReason(dto.getReason());
        request.setStatus(RequestStatus.Pending);

        //Process days and items;
        List<StaffingRequestDay> days = new ArrayList<>();
        dto.getDays().forEach(dayDto-> {
            StaffingRequestDay day = new StaffingRequestDay();
            day.setRequest(request);
            day.setDate(dayDto.getDate());

            List<StaffingRequestItem> items = new ArrayList<>();
            dayDto.getItems().forEach(itemDto->{
                StaffingRequestItem item = new StaffingRequestItem();
                item.setDay(day);

                JobRole jobRole =  jobRoleRepository.findById(itemDto.getJob_role_id())
                        .orElseThrow(()-> new RuntimeException("Job role not found with id" + itemDto.getJob_role_id()));

                JobLevel jobLevel = jobLevelRepository.findById(itemDto.getJob_level_id())
                        .orElseThrow(()-> new RuntimeException("Job level not found with id " + itemDto.getJob_level_id()));

                item.setRole(jobRole);
                item.setJobLevel(jobLevel);
                item.setRequiredCount(itemDto.getRequired_count());
                item.setStartTime(itemDto.getStart_time());
                item.setEndTime(itemDto.getEnd_time());

                items.add(item);
            });
            day.setItems(items);
            days.add(day);
        });

        request.setDays(days);
        return staffingRequestRepository.save(request);
    }

    public StaffingRequest getRequestById(Long id){
        return staffingRequestRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Request not found with id: " + id));
    }

    public List<StaffingRequest> getAllRequests(){
        return  staffingRequestRepository.findAll();
    }

}
