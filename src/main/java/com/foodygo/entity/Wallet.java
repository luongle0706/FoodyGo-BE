package com.foodygo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Wallet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    Double balance;

    @OneToOne
    Customer customer;

    @OneToMany(mappedBy = "wallet")
    List<Transaction> transactions;

    @OneToMany(mappedBy = "wallet")
    List<Deposit> deposits;
}
