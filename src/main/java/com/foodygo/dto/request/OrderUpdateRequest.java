package com.foodygo.dto.request;

import com.foodygo.enums.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderUpdateRequest {
    Double shippingFee;
    Double serviceFee;
    Double totalPrice;
    LocalDateTime expectedDeliveryTime;
    OrderStatus status;
    String customerPhone;
    String shipperPhone;
    String notes;
    MultipartFile image;
    Integer employeeId;
    Integer userId;
    Integer restaurantId;
    Integer hubId;
    List<OrderDetailUpdateRequest> orderDetailUpdateRequests;
}
