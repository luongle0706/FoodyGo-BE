package com.foodygo.dto.paging;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.foodygo.entity.Product;
import jakarta.persistence.criteria.Predicate;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonFilter("dynamicFilter")
public class ProductPagingResponse {
    Integer id;
    String code;
    String name;
    Double price;
    String description;
    Double prepareTime;
    boolean available;
    Integer restaurantId;
    String restaurantName;
    Integer categoryId;
    String categoryName;
    List<AddonSection> addonSections;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonFilter("dynamicFilter")
    public static class AddonSection {
        Integer id;
        String name;
        Integer maxChoice;
        boolean required;
    }

    public static ProductPagingResponse fromEntity(Product product) {
        return ProductPagingResponse.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .prepareTime(product.getPrepareTime())
                .available(product.isAvailable())
                .restaurantId(product.getRestaurant().getId())
                .restaurantName(product.getRestaurant().getName())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .addonSections(product.getAddonSections().stream().map(
                        as -> AddonSection.builder()
                                .id(as.getId())
                                .name(as.getName())
                                .maxChoice(as.getMaxChoice())
                                .required(as.isRequired())
                                .build()
                ).toList())
                .build();
    }

    public static Specification<Product> filterByFields(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            params.forEach((key, value) -> {
                switch (key) {
                    case "id":
                        predicates.add(criteriaBuilder.equal(root.get("id"), Integer.parseInt(value)));
                        break;
                    case "code":
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), "%" + value.toLowerCase() + "%"));
                        break;
                    case "name":
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + value.toLowerCase() + "%"));
                        break;
                    case "price":
                        predicates.add(criteriaBuilder.equal(root.get("price"), Double.parseDouble(value)));
                        break;
                    case "restaurantId":
                        predicates.add(criteriaBuilder.equal(root.get("restaurant").get("id"), Integer.parseInt(value)));
                        break;
                    case "categoryId":
                        predicates.add(criteriaBuilder.equal(root.get("category").get("id"), Integer.parseInt(value)));
                        break;
                    case "available":
                        predicates.add(criteriaBuilder.equal(root.get("available"), Boolean.parseBoolean(value)));
                        break;
                }
            });

            predicates.add(criteriaBuilder.equal(root.get("deleted"), false));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
