package com.foodygo.entity;

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
@Table(name = "products")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String code;

    String name;

    Double price;

    String description;

    Double prepareTime;

    @Builder.Default
    boolean available = true;

    @ManyToOne
    Restaurant restaurant;

    @ManyToOne
    Category category;

    @OneToMany(mappedBy = "product")
    List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "product")
    List<AddonSection> addonSections;
}
