package com.foodygo.thirdparty.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationServiceImpl implements LocationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void handleLocation(Coords coords) {
        log.info("Received location {},{}", coords.latitude(), coords.longitude());
        log.info("Distance: {}", coords.distance());
        messagingTemplate.convertAndSend("/topic/location/" + coords.orderId(), coords);
    }
}
