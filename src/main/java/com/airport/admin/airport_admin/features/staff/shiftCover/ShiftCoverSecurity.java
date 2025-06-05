package com.airport.admin.airport_admin.features.staff.shiftCover;

import com.airport.admin.airport_admin.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("shiftCoverSecurity")
public class ShiftCoverSecurity {

    private final ShiftCoverRequestRepository repository;

    public ShiftCoverSecurity(ShiftCoverRequestRepository repository) {
        this.repository = repository;
    }

    public boolean canView(Long requestId, Authentication auth) {
        Long currentUserId = ((CustomUserDetails) auth.getPrincipal()).getId();

        return repository.findById(requestId)
                .map(req -> req.getOriginalUser().getId().equals(currentUserId))
                .orElse(false);
    }

    public boolean canModify(Long requestId, Authentication auth) {
        return canView(requestId, auth); // same rule for now
    }
}
