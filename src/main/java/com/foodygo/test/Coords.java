package com.foodygo.test;

import lombok.Builder;

@Builder
public record Coords(
        int orderId,
        double latitude,
        double longitude
) {
}
