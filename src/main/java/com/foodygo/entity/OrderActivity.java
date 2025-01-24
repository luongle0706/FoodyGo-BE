package com.foodygo.entity;

import com.foodygo.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "order-activities")
public class OrderActivity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Enumerated(EnumType.STRING)
    OrderStatus fromStatus;

    @Enumerated(EnumType.STRING)
    OrderStatus toStatus;

    LocalDateTime time;

    String image;

    @ManyToOne
    User user;

    @ManyToOne
    Order order;
}
