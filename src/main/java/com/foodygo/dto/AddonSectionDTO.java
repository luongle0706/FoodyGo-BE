package com.foodygo.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record AddonSectionDTO(
        Integer id,
        String name,
        Integer maxChoice,
        boolean required,
        List<AddonItemDTO> addonItems
) {
    @Builder
    public record CreateRequest(
            String name,
            Integer maxChoice,
            boolean required,
            List<Integer> productId
    ) {
    }

    @Builder
    public record UpdateRequest(
            Integer id,
            String name,
            Integer maxChoice,
            boolean required
    ) {
    }
}
