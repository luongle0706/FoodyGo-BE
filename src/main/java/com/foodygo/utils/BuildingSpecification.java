package com.foodygo.utils;

import com.foodygo.entity.Building;
import org.springframework.data.jpa.domain.Specification;

public class BuildingSpecification {

    public static Specification<Building> searchByField(String field, String value) {
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
