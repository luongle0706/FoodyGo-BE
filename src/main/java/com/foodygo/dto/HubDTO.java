package com.foodygo.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HubDTO {
    Integer id;
    String name;
    String address;
    String description;
    double latitude;
    double longitude;
}
