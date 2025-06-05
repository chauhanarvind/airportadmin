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
        return authentication != null &&
                authentication.getPrincipal() instanceof User &&
                ((User) authentication.getPrincipal()).getId().equals(userId);
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
