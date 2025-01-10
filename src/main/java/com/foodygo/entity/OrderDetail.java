package com.foodygo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetail extends BaseEntity {

    @Id
    Integer id;

    Integer quantity;

    Double price;

    @ManyToOne
    Product product;

    @ManyToOne
    Order order;
}
