package com.airport.admin.airport_admin;

public class AvailabilityConflictException extends RuntimeException {
    public AvailabilityConflictException(String message) {
        super(message);
    }
}