package com.airport.admin.airport_admin.features.staff.staffAvailability.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TimeRangeValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTimeRange {
    String message() default "unavailableFrom must be before unavailableTo";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
