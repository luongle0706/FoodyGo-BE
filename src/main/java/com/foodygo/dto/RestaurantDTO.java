package com.foodygo.dto;

import lombok.Builder;

@Builder
public record RestaurantDTO(
        Integer id,
        String name,
        String phone,
        String email,
        String address,
        String image,
        boolean available
) {
}
