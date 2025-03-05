package com.foodygo.dto;

import lombok.Builder;

@Builder
public record AddonItemDTO(
        Integer id,
        String name,
        Double price,
        Integer quantity
) {
    @Builder
    public record CreateRequest(
            String name,
            Double price,
            Integer quantity
    ) {
    }

    @Builder
    public record UpdateRequest(
            Integer id,
            String name,
            Double price,
            Integer quantity
    ) {
    }
}
