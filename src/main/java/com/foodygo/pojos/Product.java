package com.foodygo.pojos;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends BaseEntity {

    @Id
    Integer id;

    Integer quantity;

    String name;

    Double price;

    String description;

    LocalDateTime prepareTime;

    @Builder.Default
    boolean enabled = true;

    @ManyToOne
    Restaurant restaurant;

    @ManyToOne
    Category category;

    @OneToMany(mappedBy = "product")
    List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "product")
    List<ProductAddonSection> sections;
}
