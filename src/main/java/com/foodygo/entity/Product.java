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
@Table(name = "product")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String code;

    @Column(columnDefinition = "NVARCHAR(255)")
    String name;

    Double price;

    String description;

    Double prepareTime;

    @Column(name = "image", columnDefinition = "NVARCHAR(1000)")
    String image;

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
