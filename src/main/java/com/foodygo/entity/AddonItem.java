package com.foodygo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddonItem extends BaseEntity {

    @Id
    Integer id;

    String name;

    Double price;

    Integer quantity;

    @ManyToOne
    AddonSection section;

}
