package com.foodygo.dto.paging;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.foodygo.entity.Building;
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
public class BuildingPagingResponse {
    Integer id;
    String name;
    String description;
    Integer hubId;
    double longitude;
    double latitude;

    public static BuildingPagingResponse fromEntity(Building building) {
        return BuildingPagingResponse.builder()
                .id(building.getId())
                .name(building.getName())
                .description(building.getDescription())
                .hubId(building.getHub().getId())
                .longitude(building.getLongitude())
                .latitude(building.getLatitude())
                .build();
    }

    public static Specification<Building> filterByFields(Map<String, String> params) {
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
                    case "hubId":
                        predicates.add(criteriaBuilder.equal(root.get("hub").get("id"), Integer.parseInt(value)));
                        break;
                    case "longitude":
                        predicates.add(criteriaBuilder.equal(root.get("longitude"), Double.parseDouble(value)));
                        break;
                    case "latitude":
                        predicates.add(criteriaBuilder.equal(root.get("latitude"), Double.parseDouble(value)));
                        break;
                }
            });

            predicates.add(criteriaBuilder.equal(root.get("deleted"), false));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
