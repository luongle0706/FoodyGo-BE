package com.foodygo.test;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CoordsController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/location")
    public void handleLocation(@Payload Coords coords) {
        System.out.println(coords);
        messagingTemplate.convertAndSend("/topic/location/" + coords.orderId(), coords);
    }

}
