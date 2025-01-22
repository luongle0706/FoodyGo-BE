package com.foodygo.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CategoryDTO(
        Integer id,
        String name,
        String description,
        List<Product> products,
        Restaurant restaurant
) {

    @Builder
    public record CategoryCreateRequest(
            String name,
            String description,
            Integer restaurantId
    ) {
    }

    @Builder
    public record CategoryUpdateRequest(
            Integer id,
            String name,
            String description
    ) {
    }

    @Builder
    public record Restaurant(
            Integer id,
            String name,
            String phone,
            String email,
            String address,
            String image
    ) {
    }

    @Builder
    public record Product(
            Integer id,
            String code,
            String name,
            Double price,
            String description,
            Double prepareTime,
            boolean available
    ) {
    }
}
