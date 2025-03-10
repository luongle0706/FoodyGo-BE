package com.foodygo.entity;

import com.foodygo.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "orders")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "customer-order")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(columnDefinition = "DATETIME(0)")
    LocalDateTime time;

    Double shippingFee;

    Double serviceFee;

    Double totalPrice;

    LocalDateTime confirmedAt;

    @Enumerated(EnumType.STRING)
    OrderStatus status;

    @Column(columnDefinition = "DATETIME(0)")
    LocalDateTime expectedDeliveryTime;

    String customerPhone;

    String shipperPhone;

    String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    User employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hub_id")
    Hub hub;

    @OneToMany(mappedBy = "order")
    List<Transaction> transactions;

    @OneToMany(mappedBy = "order")
    List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "order")
    List<OrderActivity> orderActivities;
}
