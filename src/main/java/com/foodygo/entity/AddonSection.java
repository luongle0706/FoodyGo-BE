package com.foodygo.entity;

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
@Table(name = "addon-section")
public class AddonSection extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(columnDefinition = "NVARCHAR(255)")
    String name;

    Integer maxChoice;

    @Builder.Default
    boolean required = false;

    @ManyToMany
    List<Product> products;

    @OneToMany(mappedBy = "section")
    List<AddonItem> items;
}
