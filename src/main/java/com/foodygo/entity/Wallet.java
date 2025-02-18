package com.foodygo.entity;

import com.foodygo.enums.WalletType;
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
@Table(name = "wallet")
public class Wallet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    Double balance;

    @Enumerated(EnumType.STRING)
    WalletType walletType;

    @OneToOne
    Customer customer;

    @ManyToOne
    Restaurant restaurant;

    @OneToMany(mappedBy = "wallet")
    List<Transaction> transactions;
}
