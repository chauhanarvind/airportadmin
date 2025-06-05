package com.airport.admin.airport_admin.features.staff.leave;

import com.airport.admin.airport_admin.features.Admin.user.User;
import com.airport.admin.airport_admin.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("leaveSecurity")
public class LeaveSecurity {

    @Autowired
    private LeaveRequestRepository leaveRepo;

    public boolean canView(Long leaveId, Authentication auth) {
        Long authId = ((CustomUserDetails) auth.getPrincipal()).getId();
        return leaveRepo.findById(leaveId)
                .map(req -> req.getUser().getId().equals(authId))
                .orElse(false);
    }

    public boolean canAccess(Long leaveId, Authentication authentication) {
        LeaveRequest leave = leaveRepo.findById(leaveId).orElse(null);
        if (leave == null) return false;

        User user = (User) authentication.getPrincipal();
        return leave.getUser().getId().equals(user.getId());
    }
}
