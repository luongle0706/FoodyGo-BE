package com.foodygo.thirdparty.websocket;

import lombok.Builder;

@Builder
public record Coords(
        int orderId,
        double latitude,
        double longitude,
        double distance
) {
}
