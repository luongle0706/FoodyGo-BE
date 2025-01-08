package com.foodygo.pojos;

import com.foodygo.enums.ProductAddonType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductAddonSection extends BaseEntity {

    @Id
    Integer id;

    String name;

    Integer maxChoice;

    @Enumerated(EnumType.STRING)
    ProductAddonType type;

    @ManyToOne
    Product product;

    @Builder.Default
    boolean required = false;

    @OneToMany(mappedBy = "section")
    List<ProductAddonItem> items;
}
