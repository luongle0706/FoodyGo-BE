package com.foodygo.dto.paging;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.foodygo.entity.Category;
import jakarta.persistence.criteria.Predicate;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonFilter("dynamicFilter")
public class CategoryPagingResponse {
    Integer id;
    String name;
    String description;
    Integer restaurantId;
    String restaurantName;
    List<Product> products;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Product {
        Integer id;
        String code;
        String name;
    }

    public static CategoryPagingResponse fromEntity(Category category) {
        return CategoryPagingResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .restaurantId(category.getRestaurant().getId())
                .restaurantName(category.getRestaurant().getName())
                .products(category.getProducts().stream().map(
                        p -> Product.builder()
                                .id(p.getId())
                                .code(p.getCode())
                                .name(p.getName())
                                .build()
                ).toList())
                .build();
    }

    public static Specification<Category> filterByFields(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            params.forEach((key, value) -> {
                switch (key) {
                    case "id":
                        predicates.add(criteriaBuilder.equal(root.get("id"), Integer.parseInt(value)));
                        break;
                    case "name":
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + value.toLowerCase() + "%"));
                        break;
                    case "description":
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + value.toLowerCase() + "%"));
                        break;
                    case "restaurantId":
                        predicates.add(criteriaBuilder.equal(root.get("restaurant").get("id"), Integer.parseInt(value)));
                        break;
                    case "restaurantName":
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("restaurant").get("name")), "%" + value.toLowerCase() + "%"));
                        break;
                }
            });

            predicates.add(criteriaBuilder.equal(root.get("deleted"), false));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
