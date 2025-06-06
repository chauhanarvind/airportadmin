package com.airport.admin.airport_admin.security;

import com.airport.admin.airport_admin.features.Admin.user.User;
import com.airport.admin.airport_admin.features.Admin.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    private final UserRepository userRepository;

    public SecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isOwner(Long userId, Authentication authentication) {
        if (authentication == null) {
            System.out.println("Authentication is null");
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            System.out.println("Principal is not of type User: " + principal.getClass().getName());
            return false;
        }

        Long authUserId = ((User) principal).getId();
        System.out.println("Checking isOwner:");
        System.out.println(" - Requested userId: " + userId);
        System.out.println(" - Authenticated userId: " + authUserId);

        return authUserId.equals(userId);
    }


    public boolean isAdmin(Authentication authentication) {
        return authentication != null &&
                authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_Admin"));
    }

    public boolean canAccessUserData(Long userId, Authentication authentication) {
        return isOwner(userId, authentication) || isAdmin(authentication);
    }

    public boolean canModifyEntity(Long entityOwnerId, Authentication authentication) {
        return isAdmin(authentication) || isOwner(entityOwnerId, authentication);
    }
}
