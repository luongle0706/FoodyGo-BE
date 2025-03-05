package com.foodygo.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantResponseDTO {
    Integer id;
    String name;
    String phone;
    String email;
    String address;
    String image;
    boolean available;
}
