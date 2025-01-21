package com.foodygo.entity;

import com.foodygo.enums.DepositMethod;
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
public class Transaction extends BaseEntity {

    @Id
    Integer id;

    String description;

    LocalDateTime time;

    Double amount;

    Double remaining;

    @ManyToOne
    Order order;

    @ManyToOne
    Wallet wallet;

    @OneToOne(mappedBy = "transaction")
    Deposit deposit;
}
