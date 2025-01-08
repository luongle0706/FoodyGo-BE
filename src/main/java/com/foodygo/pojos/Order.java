package com.foodygo.pojos;

import com.foodygo.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order extends BaseEntity {

    @Id
    Integer id;

    LocalDateTime time;

    Double shippingFee;

    Double serviceFee;

    Double totalPrice;

    LocalDateTime expectedDeliveryTime;

    @Enumerated(EnumType.STRING)
    OrderStatus status;

    @ManyToOne
    User user;

    @ManyToOne
    Restaurant restaurant;

    @ManyToOne
    Payment payment;

    @OneToMany
    List<OrderDetail> orderDetails;
}
