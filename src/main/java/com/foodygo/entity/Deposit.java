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
public class Deposit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String description;

    Double amount;

    LocalDateTime time;

    @Enumerated(EnumType.STRING)
    DepositMethod method;

    Double remaining;

    @ManyToOne
    Wallet wallet;

    @OneToOne
    Transaction transaction;
}
