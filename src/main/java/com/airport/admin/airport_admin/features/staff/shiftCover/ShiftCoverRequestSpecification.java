package com.airport.admin.airport_admin.features.staff.shiftCover;

import com.airport.admin.airport_admin.enums.CoverRequestStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ShiftCoverRequestSpecification {

    public static Specification<ShiftCoverRequest> build(
            Long originalUserId,
            Long coveringUserId,
            CoverRequestStatus status,
            LocalDate shiftDate
    ) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (originalUserId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("originalUser").get("id"), originalUserId));
            }

            if (coveringUserId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("coveringUser").get("id"), coveringUserId));
            }

            if (status != null) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), status));
            }

            if (shiftDate != null) {
                predicate = cb.and(predicate, cb.equal(root.get("shiftDate"), shiftDate));
            }

            return predicate;
        };
    }
}
