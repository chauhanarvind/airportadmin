package com.airport.admin.airport_admin.services;

import com.airport.admin.airport_admin.models.StaffingRequest;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class StaffingRequestSpecification {

    public static Specification<StaffingRequest> build(
            String managerName,
            Long managerId,
            Long locationId
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (managerName != null && !managerName.isEmpty()) {
                predicates.add(cb.like(
                        cb.lower(root.get("manager").get("name")),
                        "%" + managerName.trim().toLowerCase() + "%"
                ));
            }

            if (managerId != null) {
                predicates.add(cb.equal(root.get("manager").get("id"), managerId));
            }

            if (locationId != null) {
                predicates.add(cb.equal(root.get("location").get("id"), locationId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
