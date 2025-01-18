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
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String image;

    @ManyToOne
    Building building;

    @OneToOne
    User user;

    @OneToOne(mappedBy = "customer")
    Wallet wallet;

    @OneToMany(mappedBy = "customer")
    List<Order> orders;
}
