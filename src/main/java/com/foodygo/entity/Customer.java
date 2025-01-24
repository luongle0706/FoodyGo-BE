package com.foodygo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "customers")
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "image", columnDefinition = "VARCHAR(1000)")
    String image;

    @ManyToOne
    Building building;

    @OneToOne
    @JsonBackReference
    User user;

    @OneToOne(mappedBy = "customer")
    Wallet wallet;

    @OneToMany(mappedBy = "customer")
    @JsonBackReference
    List<Order> orders;

}
