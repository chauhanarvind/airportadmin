package com.airport.admin.airport_admin.services;

import com.airport.admin.airport_admin.enums.RosterStatus;
import com.airport.admin.airport_admin.models.StaffingRequest;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class StaffingRequestSpecification {

    public static Specification<StaffingRequest> build(
            Long managerId,
            Long locationId,
            String status
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (managerId != null) {
                predicates.add(cb.equal(root.get("manager").get("id"), managerId));
            }

            if (locationId != null) {
                predicates.add(cb.equal(root.get("location").get("id"), locationId));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), RosterStatus.valueOf(status)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
