package com.foodygo.entity;

import com.foodygo.enums.DepositMethod;
import com.foodygo.enums.DepositStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "deposit")
public class Deposit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(columnDefinition = "NVARCHAR(2000)")
    String description;

    Double amount;

    @Column(columnDefinition = "DATETIME(0)")
    LocalDateTime time;

    @Enumerated(EnumType.STRING)
    DepositMethod method;

    Double remaining;

    @Enumerated(EnumType.STRING)
    DepositStatus status = DepositStatus.PENDING;

    @OneToMany(mappedBy = "deposit")
    List<Transaction> transactions;

    @ManyToOne
    Wallet wallet;

    @ManyToOne
    Customer customer;
}
