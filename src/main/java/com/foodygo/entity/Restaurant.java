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
@Table(name = "restaurant")
public class Restaurant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(columnDefinition = "NVARCHAR(255)")
    String name;

    String phone;

    String email;

    String address;

    String image;

    @Column(columnDefinition = "DOUBLE DEFAULT 0.0")
    double longitude;

    @Column(columnDefinition = "DOUBLE DEFAULT 0.0")
    double latitude;

    @OneToOne
    User owner;

    @OneToOne(mappedBy = "restaurant")
    Wallet wallet;

    @Builder.Default
    boolean available = true;

    @OneToMany(mappedBy = "restaurant")
    List<Order> orders;

    @OneToMany(mappedBy = "restaurant")
    List<Product> products;

    @OneToMany(mappedBy = "restaurant")
    List<Category> categories;

    @OneToMany(mappedBy = "restaurant")
    private List<OperatingHour> operatingHours;
}
