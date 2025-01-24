package com.foodygo.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildingDTO {
    Integer id;
    String name;
    String description;
    HubDTO hub;
}
