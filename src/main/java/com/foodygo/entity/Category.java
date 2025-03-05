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
@Table(name = "category")
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(columnDefinition = "NVARCHAR(255)")
    String name;

    @Column(columnDefinition = "NVARCHAR(2000)")
    String description;

    @OneToMany(mappedBy = "category")
    List<Product> products;

    @ManyToOne
    Restaurant restaurant;
}
