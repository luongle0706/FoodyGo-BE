package com.foodygo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Building extends BaseEntity {

    @Id
    Integer id;

    String name;

    String description;

    @ManyToOne
    Hub hub;

    @OneToMany(mappedBy = "building")
    List<Customer> customers;
}
