package com.airport.admin.airport_admin.features.staff.staffAvailability;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;

public class StaffAvailabilitySpecification {
    public static Specification<StaffAvailability> build(Long userId, LocalDate date) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (userId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("user").get("id"), userId));
            }

            if (date != null) {
                predicate = cb.and(predicate, cb.equal(root.get("date"), date));
            }

            return predicate;
        };
    }
}
