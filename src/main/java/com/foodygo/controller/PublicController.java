package com.foodygo.controller;

import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.service.spec.BuildingService;
import com.foodygo.service.spec.HubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {
    private final HubService hubService;
    private final BuildingService buildingService;

    @GetMapping("/hubs")
    public ResponseEntity<ObjectResponse> getAllHubs() {
        return ResponseEntity.ok(
                ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Get all hubs")
                        .data(hubService.getHubsForSelection())
                        .build()
        );
    }

    @GetMapping("/buildings")
    public ResponseEntity<ObjectResponse> getAllBuildings() {
        return ResponseEntity.ok(
                ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Get all buildings")
                        .data(buildingService.getAllBuildings())
                        .build()
        );
    }
}
