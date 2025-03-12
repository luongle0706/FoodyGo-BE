package com.foodygo.dto.paging;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.foodygo.entity.AddonSection;
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
public class AddonSectionPagingResponse {
    Integer id;
    String name;
    Integer maxChoice;
    boolean required;
    List<AddonItemResponse> items;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class AddonItemResponse {
        Integer id;
        String name;
        Double price;
        Integer quantity;
    }

    public static AddonSectionPagingResponse fromEntity(AddonSection section) {
        return AddonSectionPagingResponse.builder()
                .id(section.getId())
                .name(section.getName())
                .maxChoice(section.getMaxChoice())
                .required(section.isRequired())
                .items(section.getItems().stream().map(item ->
                        AddonItemResponse.builder()
                                .id(item.getId())
                                .name(item.getName())
                                .price(item.getPrice())
                                .quantity(item.getQuantity())
                                .build()
                ).toList())
                .build();
    }

    public static Specification<AddonSection> filterByFields(Map<String, String> params) {
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
                    case "maxChoice":
                        predicates.add(criteriaBuilder.equal(root.get("maxChoice"), Integer.parseInt(value)));
                        break;
                    case "required":
                        predicates.add(criteriaBuilder.equal(root.get("required"), Boolean.parseBoolean(value)));
                        break;
                    case "addonItemName":
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("items").get("name")), "%" + value.toLowerCase() + "%"));
                        break;
                    case "addonItemPriceMin":
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.join("items").get("price"), Double.parseDouble(value)));
                        break;
                    case "addonItemPriceMax":
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.join("items").get("price"), Double.parseDouble(value)));
                        break;
                }
            });
            predicates.add(criteriaBuilder.equal(root.get("deleted"), false)); // Exclude deleted orders
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

