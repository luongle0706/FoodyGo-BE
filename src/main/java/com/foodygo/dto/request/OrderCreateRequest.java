package com.foodygo.dto.request;

import com.foodygo.entity.Order;
import com.foodygo.enums.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreateRequest {
    Double shippingFee;
    Double productPrice;
    LocalDateTime expectedDeliveryTime;
    LocalDateTime time;
    String customerPhone;
    String notes;
    Integer employeeId;
    Integer customerId;
    Integer restaurantId;
    Integer hubId;
    List<OrderDetailCreateRequest> orderDetails;

}
