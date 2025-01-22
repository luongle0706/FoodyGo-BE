package com.foodygo.entity;

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
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    LocalDateTime time;

    Double shippingFee;

    Double serviceFee;

    Double totalPrice;

    @Enumerated(EnumType.STRING)
    OrderStatus status;

    LocalDateTime expectedDeliveryTime;

    String customerPhone;

    String shipperPhone;

    String notes;

    @ManyToOne
    User employee;

    @ManyToOne
    Customer customer;

    @ManyToOne
    Restaurant restaurant;

    @ManyToOne
    Hub hub;

    @OneToMany(mappedBy = "order")
    List<Transaction> transactions;

    @OneToMany(mappedBy = "order")
    List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "order")
    List<OrderActivity> orderActivities;
}
