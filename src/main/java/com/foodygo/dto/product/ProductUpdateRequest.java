package com.foodygo.dto.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {
    String code;
    String name;
    Double price;
    String description;
    Double prepareTime;
    Boolean available;
    Integer categoryId;
}
