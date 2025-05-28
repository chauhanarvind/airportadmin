package com.airport.admin.airport_admin.features.staffAvailability.validation;

import com.airport.admin.airport_admin.features.staffAvailability.dto.StaffAvailabilityRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;

public class TimeRangeValidator implements ConstraintValidator<ValidTimeRange, StaffAvailabilityRequestDto> {

    @Override
    public boolean isValid(StaffAvailabilityRequestDto dto, ConstraintValidatorContext context) {
        if (dto.getIsAvailable() == null || dto.getIsAvailable()) {
            return true; // Skip validation if user is marked as available
        }

        LocalTime from = dto.getUnavailableFrom();
        LocalTime to = dto.getUnavailableTo();

        if (from == null || to == null) return true; // Let other annotations handle required fields

        return from.isBefore(to);
    }
}
