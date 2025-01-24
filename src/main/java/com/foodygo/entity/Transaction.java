package com.foodygo.entity;

import com.foodygo.enums.DepositMethod;
import com.foodygo.enums.TransactionType;
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
@Table(name = "transactions")
public class Transaction extends BaseEntity {

    @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String description;

    LocalDateTime time;

    Double amount;

    Double remaining;

    @Enumerated(EnumType.STRING)
    TransactionType type;

    @ManyToOne
    Order order;

    @ManyToOne
    Wallet wallet;

    @ManyToOne
    Deposit deposit;
}
