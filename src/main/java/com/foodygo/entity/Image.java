package com.foodygo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Image {

    @Id
    Integer id;

    String url;

    @ManyToOne
    User user;

    @ManyToOne
    Restaurant restaurant;

    @ManyToOne
    Product product;
}
