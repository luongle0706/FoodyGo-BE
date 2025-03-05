package com.foodygo.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDTO {
    Integer id;
    String image;
    BuildingDTO building;
    UserDTO user;
}
