package com.foodygo.dto.request;

import com.foodygo.entity.*;
import com.foodygo.enums.OrderStatus;
import jakarta.persistence.*;
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
    Double serviceFee;
    Double totalPrice;
    LocalDateTime expectedDeliveryTime;
    LocalDateTime time;
    String customerPhone;
    String shipperPhone;
    String notes;
    Integer employeeId;
    Integer customerId;
    Integer restaurantId;
    Integer hubId;
    List<OrderDetailCreateRequest> orderDetails;

}
