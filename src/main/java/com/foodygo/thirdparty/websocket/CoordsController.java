package com.foodygo.thirdparty.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CoordsController {

    private final LocationService locationService;

    @MessageMapping("/location")
    public void handleLocation(@Payload Coords coords) {
        locationService.handleLocation(coords);
    }

}
