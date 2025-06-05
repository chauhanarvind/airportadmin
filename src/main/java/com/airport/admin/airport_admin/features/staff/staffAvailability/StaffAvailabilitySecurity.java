package com.airport.admin.airport_admin.features.staff.staffAvailability;

import com.airport.admin.airport_admin.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("availabilitySecurity")
public class StaffAvailabilitySecurity {

    private final StaffAvailabilityRepository repository;

    public StaffAvailabilitySecurity(StaffAvailabilityRepository repository) {
        this.repository = repository;
    }

    public boolean canView(Long id, Authentication auth) {
        Long currentUserId = ((CustomUserDetails) auth.getPrincipal()).getId();
        return repository.findById(id)
                .map(record -> record.getUser().getId().equals(currentUserId))
                .orElse(false);
    }

    public boolean canModify(Long id, Authentication auth) {
        return canView(id, auth); // reuse logic
    }
}
