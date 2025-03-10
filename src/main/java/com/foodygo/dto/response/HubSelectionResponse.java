package com.foodygo.dto.response;

import com.foodygo.entity.Hub;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HubSelectionResponse {
    Integer id;
    String name;
    String address;
    double longitude;
    double latitude;

    public static HubSelectionResponse fromEntity(Hub hub) {
        return HubSelectionResponse.builder()
                .id(hub.getId())
                .name(hub.getName())
                .address(hub.getAddress())
                .longitude(hub.getLongitude())
                .latitude(hub.getLatitude())
                .build();
    }
}
