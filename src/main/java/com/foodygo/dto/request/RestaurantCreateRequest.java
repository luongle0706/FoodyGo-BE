package com.foodygo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantCreateRequest {
    Integer id;
    String name;
    String phone;
    String email;
    String address;
    String image;
    boolean available;
    Integer ownerId;
}
