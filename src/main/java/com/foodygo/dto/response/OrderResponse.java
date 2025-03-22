package com.foodygo.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Integer id;
    Double shippingFee;
    Double serviceFee;
    Double totalPrice;
    Integer totalItems;
    String status;
    LocalDateTime expectedDeliveryTime;
    LocalDateTime time;
    LocalDateTime confirmedAt;
    String customerPhone;
    String shipperPhone;
    String notes;
    String employeeName;
    String customerName;
    String restaurantName;
    Integer restaurantId;
    String image;
    String hubName;
    List<OrderDetailResponse> orderDetails;
}