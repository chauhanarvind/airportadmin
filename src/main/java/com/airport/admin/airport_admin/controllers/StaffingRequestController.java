package com.airport.admin.airport_admin.controllers;

import com.airport.admin.airport_admin.dto.StaffingRequestsDto;
import com.airport.admin.airport_admin.models.StaffingRequest;
import com.airport.admin.airport_admin.services.StaffingRequestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class StaffingRequestController {
    private final StaffingRequestService staffingRequestService;

    public StaffingRequestController(StaffingRequestService staffingRequestService){
        this.staffingRequestService = staffingRequestService;
    }




    @GetMapping
    public ResponseEntity<List<StaffingRequest>> getAllRequests(){
        List<StaffingRequest> requests  = staffingRequestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffingRequest> getRequestById(@PathVariable Long id){
        StaffingRequest request = staffingRequestService.getRequestById(id);
        return ResponseEntity.ok(request);
    }
}
