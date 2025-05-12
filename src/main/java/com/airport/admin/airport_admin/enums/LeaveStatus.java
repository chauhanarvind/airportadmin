package com.airport.admin.airport_admin.enums;

public enum LeaveStatus {
    PENDING,      // Submitted for review, awaiting admin decision
    APPROVED,     // Approved by admin
    REJECTED,     // Rejected by admin
    CANCELLED,    // Cancelled by user before admin decision
    RESUBMITTED   // Edited and resubmitted after rejection or cancellation
}