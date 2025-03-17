package com.foodygo.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponse {
    Integer id;
    Integer orderId;
    Integer quantity;
    Double price;
    String addonItems;
    String image;
    String productName;
}
