package com.foodygo.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ProductDTO(
        Integer id,
        String code,
        String name,
        Double price,
        String description,
        Double prepareTime,
        boolean available,
        List<AddonSection> addonSections
) {
    public record AddonSection(
            Integer id,
            String name,
            Integer maxChoice,
            boolean required,
            List<AddonItem> items
    ) {
        public record AddonItem(
                Integer id,
                String name,
                Double price,
                Integer quantity
        ) {
        }
    }
}
