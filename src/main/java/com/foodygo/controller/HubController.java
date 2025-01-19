package com.foodygo.controller;

import com.foodygo.dto.request.HubCreateRequest;
import com.foodygo.dto.request.HubUpdateRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.entity.Hub;
import com.foodygo.service.HubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sdw391/v1/hub")
public class HubController {

    private final HubService hubService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all")
    public ResponseEntity<ObjectResponse> getAllHubs() {
        return !hubService.findAll().isEmpty() ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all hubs successfully", hubService.findAll())) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Get all hubs failed", null));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all-active")
    public ResponseEntity<ObjectResponse> getAllHubsActive() {
        return !hubService.getHubsActive().isEmpty() ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all hubs active successfully", hubService.getHubsActive())) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Get all hubs active failed", null));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/undelete/{hub-id}")
    public ResponseEntity<ObjectResponse> unDeleteHubByID(@PathVariable("hub-id") int hubID) {
        return hubService.undeleteHub(hubID) != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Undelete hub successfully", hubService.findById(hubID))) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Undelete hub failed", null));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get/{hub-id}")
    public ResponseEntity<ObjectResponse> getBuildingByID(@PathVariable("hub-id") int hubID) {
        return hubService.findById(hubID) != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get hub by ID successfully", hubService.findById(hubID))) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Get hub by ID failed", null));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<ObjectResponse> createHub(@RequestBody HubCreateRequest hubCreateRequest) {
        Hub hub = Hub.builder()
                .name(hubCreateRequest.getName())
                .block(hubCreateRequest.getBlock())
                .build();
        return hub != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Create hub successfully", hubService.save(hub))) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Create hub failed", null));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update/{hub-id}")
    public ResponseEntity<ObjectResponse> updateHub(@PathVariable("hub-id") int hubID, @RequestBody HubUpdateRequest hubUpdateRequest) {
        Hub hub = hubService.findById(hubID);
        if (hub != null) {
            if (hubUpdateRequest.getName() != null) {
                hub.setName(hubUpdateRequest.getName());
            }
            if (hubUpdateRequest.getBlock() != null) {
                hub.setBlock(hubUpdateRequest.getBlock());
            }
         return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Update hub successfully", hubService.save(hub)));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Update hub failed", null));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{hub-id}")
    public ResponseEntity<ObjectResponse> deleteHubByID(@PathVariable("hub-id") int hubID) {
        Hub hub = hubService.findById(hubID);
        if(hub != null) {
            hub.setDeleted(true);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Delete hub successfully", hubService.save(hub)));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Delete hub failed", null));
    }

}
