package com.foodygo.pojos;

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
public class Restaurant extends BaseEntity {

    @Id
    Integer id;

    String name;

    String phone;

    String email;

    String openHours;

    @Builder.Default
    boolean open = true;

    @ManyToOne
    Address address;

    @OneToMany(mappedBy = "restaurant")
    List<Image> images;

    @OneToMany(mappedBy = "restaurant")
    List<Order> orders;

    @OneToMany(mappedBy = "restaurant")
    List<Product> products;
}
