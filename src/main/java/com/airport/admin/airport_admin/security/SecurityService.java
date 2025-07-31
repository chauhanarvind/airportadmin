package com.airport.admin.airport_admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.airport.admin.airport_admin.features.staff.shiftCover.ShiftCoverRequestRepository;

@Component
public class SecurityService {
    @Autowired
    private ShiftCoverRequestRepository coverRequestRepo;
    /**
     * Returns the ID of the currently authenticated user.
     * Throws IllegalStateException if unauthenticated or unsupported principal type.
     */
    public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getAuthenticatedUserId(authentication);
    }

    /**
     * Returns the authenticated user ID from the given Authentication.
     */
    public Long getAuthenticatedUserId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails customUserDetails) {
            return customUserDetails.getId();
        }

        throw new IllegalStateException("Unsupported principal type: " + principal.getClass().getName());
    }

    /**
     * Checks if the given userId belongs to the currently authenticated user.
     */
    public boolean isOwner(Long userId, Authentication authentication) {
        try {
            Long authUserId = getAuthenticatedUserId(authentication);
            return authUserId.equals(userId);
        } catch (IllegalStateException e) {
            return false;
        }
    }

    /**
     * Checks if the authenticated user has the Admin role.
     */
    public boolean isAdmin(Authentication authentication) {
        return authentication != null &&
                authentication.getAuthorities().stream()
                        .anyMatch(auth -> "ROLE_Admin".equals(auth.getAuthority()));
    }

    /**
     * Checks if the user can access data belonging to the given userId.
     */
    public boolean canAccessUserData(Long userId, Authentication authentication) {
        return isOwner(userId, authentication) || isAdmin(authentication);
    }

    /**
     * Checks if the user can modify an entity owned by the given userId.
     */
    public boolean canModifyEntity(Long entityOwnerId, Authentication authentication) {
        return isAdmin(authentication) || isOwner(entityOwnerId, authentication);
    }

    public boolean hasRole(String roleName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null &&
                auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_" + roleName));
    }



    public boolean isOwnerOfCoverRequest(Long requestId, Authentication authentication) {
        try {
            Long authUserId = getAuthenticatedUserId(authentication);
            return coverRequestRepo.existsByIdAndOriginalUserId(requestId, authUserId);
        } catch (IllegalStateException e) {
            return false;
        }
    }


}
