package com.foodygo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Hub extends BaseEntity {

    @Id
    Integer id;

    String name;

    String block;

    @OneToOne(mappedBy = "hub")
    Address address;

    @ManyToMany(mappedBy = "hubs")
    Set<User> users;
}
