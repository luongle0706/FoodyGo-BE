package com.foodygo.entity;

import com.foodygo.enums.DepositMethod;
import jakarta.persistence.*;
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

    @ManyToOne
    Wallet wallet;

    @OneToMany(mappedBy = "deposit")
    List<Transaction> transactions;

    @ManyToOne
    Customer customer;
}
