package com.foodygo.utils;

import com.foodygo.entity.Hub;
import org.springframework.data.jpa.domain.Specification;

public class HubSpecification {

    public static Specification<Hub> searchByField(String field, String value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null || value.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            switch (field) {
                case "name":
                    return criteriaBuilder.like(root.get("name"), "%" + value + "%");
                default:
                    return null;
            }
        };
    }

}
