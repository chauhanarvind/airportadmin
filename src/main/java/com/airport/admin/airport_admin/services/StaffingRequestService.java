package com.airport.admin.airport_admin.services;

import com.airport.admin.airport_admin.models.*;
import com.airport.admin.airport_admin.repositories.*;
import org.springframework.stereotype.Service;

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

    public StaffingRequest getRequestById(Long id){
        return staffingRequestRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Request not found with id: " + id));
    }

    public List<StaffingRequest> getAllRequests(){
        return  staffingRequestRepository.findAll();
    }

}
