package com.foodygo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreateRequest {
    Integer id;
    String code;
    String name;
    Double price;
    String image;
    String description;
    Double prepareTime;
    boolean available;
    Integer restaurantId;
    Integer addonSectionId;
    Integer categoryId;
}
