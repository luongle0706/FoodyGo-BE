package com.foodygo.entity;

import com.foodygo.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "account-transaction")
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(columnDefinition = "NVARCHAR(2000)")
    String description;

    @Column(columnDefinition = "DATETIME(0)")
    LocalDateTime time = LocalDateTime.now();

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
