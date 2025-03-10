package com.foodygo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "hub")
public class Hub extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "name", nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
    String name;

    @Column(name = "address", nullable = false, columnDefinition = "VARCHAR(1000)")
    String address;

    @Column(name = "description", columnDefinition = "VARCHAR(1000)")
    String description;

    @OneToMany(mappedBy = "hub")
    List<User> employees;

    @OneToMany(mappedBy = "hub")
    @JsonBackReference
    List<Order> orders;

    @OneToMany(mappedBy = "hub")
    @JsonBackReference
    List<Building> buildings;
}
