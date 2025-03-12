package com.foodygo.dto.paging;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.foodygo.entity.Hub;
import jakarta.persistence.criteria.Predicate;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonFilter("dynamicFilter")
public class HubPagingResponse {
    Integer id;
    String name;
    String address;
    double longitude;
    double latitude;

    public static HubPagingResponse fromEntity(Hub hub) {
        return HubPagingResponse.builder()
                .id(hub.getId())
                .name(hub.getName())
                .address(hub.getAddress())
                .longitude(hub.getLongitude())
                .latitude(hub.getLatitude())
                .build();
    }

    public static Specification<Hub> filterByFields(Map<String, String> params) {
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
                    case "address":
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), "%" + value.toLowerCase() + "%"));
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
