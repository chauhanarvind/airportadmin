package com.airport.admin.airport_admin.features.Admin.user.dto;

// will use this to expose users for other apis without revealing important details
// for dropdowns
public record UserSummaryDto
        (Long id, String firstName, String lastName, String roleName) {}
