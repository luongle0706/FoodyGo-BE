package com.foodygo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailUpdateRequest {
    int id;
    int quantity;
    Double price;
    String addonItems;
    int productId;
}
