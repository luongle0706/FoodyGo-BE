package com.foodygo.pojos;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Payment extends BaseEntity {

    @Id
    Integer id;

    Double amount;

    LocalDateTime time;

    @ManyToOne
    Order order;

    @ManyToOne
    PaymentMethod paymentMethod;

    @ManyToOne
    User user;

    @OneToMany(mappedBy = "payment")
    List<Order> orders;
}
