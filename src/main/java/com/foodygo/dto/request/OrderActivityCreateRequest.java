package com.foodygo.dto.request;

import com.foodygo.enums.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderActivityCreateRequest {
    Integer orderId;
    Integer userId;
    OrderStatus fromStatus;
    OrderStatus toStatus;
    String image;
}
