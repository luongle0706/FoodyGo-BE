package com.foodygo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address extends BaseEntity {

    @Id
    Integer id;

    String longtitude;

    String latitude;

    String description;

    @OneToOne
    Hub hub;

    @OneToOne
    Restaurant restaurant;
}
