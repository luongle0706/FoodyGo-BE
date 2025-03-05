package com.foodygo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderDetailCreateRequest {
    Integer quantity;
    Double price;
    String addonItems;
    Integer productId;
}
