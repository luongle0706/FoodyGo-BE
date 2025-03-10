package com.foodygo.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.foodygo.entity.Restaurant;
import jakarta.persistence.criteria.Predicate;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonFilter("dynamicFilter")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantDTO {
    Integer id;
    String name;
    String phone;
    String email;
    String address;
    String image;
    boolean available;

    public static Specification<Restaurant> filterByFields(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            params.forEach((key, value) -> {
                switch (key) {
                    case "id":
                        predicates.add(criteriaBuilder.equal(root.get("id"), Integer.parseInt(value)));
                        break;
                    case "name":
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%%" + value.toLowerCase() + "%%"));
                        break;
                    case "phone":
                        predicates.add(criteriaBuilder.like(root.get("phone"), "%%" + value + "%%"));
                        break;
                    case "email":
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%%" + value.toLowerCase() + "%%"));
                        break;
                    case "address":
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), "%%" + value.toLowerCase() + "%%"));
                        break;
                    case "available":
                        predicates.add(criteriaBuilder.equal(root.get("available"), Boolean.parseBoolean(value)));
                        break;
                }
            });

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
