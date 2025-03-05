package com.foodygo.dto.response;

import com.foodygo.enums.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderActivityResponse {
    Integer id;
    OrderStatus fromStatus;
    OrderStatus toStatus;
    LocalDateTime time;
    String image;
    String userName;
    Integer userId;
    Integer orderId;
}
