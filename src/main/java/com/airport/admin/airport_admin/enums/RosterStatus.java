package com.airport.admin.airport_admin.enums;

public enum RosterStatus {
    PENDING,      // Submitted for review, awaiting admin decision
    APPROVED,     // Approved by admin
    REJECTED,     // Rejected by admin
    CANCELLED,    // Cancelled by user before admin decision
    RESUBMITTED
}
