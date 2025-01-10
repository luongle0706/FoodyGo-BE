package com.foodygo.entity;

import com.foodygo.enums.PaymentMethod;
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
public class Payment extends BaseEntity {

    @Id
    Integer id;

    Double amount;

    LocalDateTime time;

    @OneToOne(mappedBy = "payment")
    Order order;

    @Enumerated(EnumType.STRING)
    PaymentMethod method;

    Integer userId;
}
