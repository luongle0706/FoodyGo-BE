package com.foodygo.entity;

import com.foodygo.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "order-activity")
public class OrderActivity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Enumerated(EnumType.STRING)
    OrderStatus fromStatus;

    @Enumerated(EnumType.STRING)
    OrderStatus toStatus;

    @Column(columnDefinition = "DATETIME(0)")
    LocalDateTime time;

    String image;

    @ManyToOne
    User user;

    @ManyToOne
    Order order;
}
