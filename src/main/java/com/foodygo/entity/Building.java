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

    @Column(name = "name", nullable = false, unique = true, columnDefinition = "NVARCHAR(255)")
    String name;

    @Column(name = "description", columnDefinition = "NVARCHAR(1000)")
    String description;

    @Column(columnDefinition = "DOUBLE DEFAULT 0.0")
    double longitude;

    @Column(columnDefinition = "DOUBLE DEFAULT 0.0")
    double latitude;

    @ManyToOne
    Hub hub;

    @OneToMany(mappedBy = "building")
    @JsonBackReference
    List<Customer> customers;

}
