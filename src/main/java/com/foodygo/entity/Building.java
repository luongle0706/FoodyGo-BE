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
@Table(name = "building")
public class Building extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "name", nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
    String name;

    @Column(name = "description", columnDefinition = "VARCHAR(1000)")
    String description;

    @ManyToOne
    Hub hub;

    @OneToMany(mappedBy = "building")
    @JsonBackReference
    List<Customer> customers;

}
