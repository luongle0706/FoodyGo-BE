package com.foodygo.dto.response;

import com.foodygo.entity.Building;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublicBuildingDTO {
    Integer id;
    String name;
    String description;
    Integer hubId;
    double longitude;
    double latitude;

    public static PublicBuildingDTO fromEntity(Building building) {
        return PublicBuildingDTO.builder()
                .id(building.getId())
                .name(building.getName())
                .description(building.getDescription())
                .hubId(building.getHub().getId())
                .longitude(building.getLongitude())
                .latitude(building.getLatitude())
                .build();
    }
}
