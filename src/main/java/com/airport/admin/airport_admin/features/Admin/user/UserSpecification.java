package com.airport.admin.airport_admin.features.Admin.user;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

public class UserSpecification { //query to filter
    public static Specification<User> build(Long userId, String name, String email) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (userId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("id"), userId));
            }

            if (name != null && !name.isBlank()) {
                String likeValue = "%" + name.toLowerCase() + "%";
                Predicate firstNameLike = cb.like(cb.lower(root.get("firstName")), likeValue);
                Predicate lastNameLike = cb.like(cb.lower(root.get("lastName")), likeValue);
                predicate = cb.and(predicate, cb.or(firstNameLike, lastNameLike));
            }

            if (email != null && !email.isBlank()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }

            return predicate;
        };
    }
}
