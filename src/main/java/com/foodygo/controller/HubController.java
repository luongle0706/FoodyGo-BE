package com.foodygo.controller;

import com.foodygo.dto.request.HubCreateRequest;
import com.foodygo.dto.request.HubUpdateRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.entity.Hub;
import com.foodygo.service.HubService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/sdw391/v1/hub")
public class HubController {

    private final HubService hubService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/test")
    public ResponseEntity<ObjectResponse> getTest() {

        List<Hub> list = hubService.getTest(null);

        return !hubService.findAll().isEmpty() ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all hubs successfully", hubService.findAll())) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Get all hubs failed", null));
    }

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
    public ResponseEntity<ObjectResponse> createHub(@Valid @RequestBody HubCreateRequest hubCreateRequest) {
        try {
            Hub hub = hubService.createHub(hubCreateRequest);
            if (hub != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Create hub successfully", hub));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Create hub failed. Hub is null", null));
        } catch (Exception e) {
            log.error("Error creating hub", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Create hub failed", null));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update/{hub-id}")
    public ResponseEntity<ObjectResponse> updateHub(@PathVariable("hub-id") int hubID, @RequestBody HubUpdateRequest hubUpdateRequest) {
        try {
            Hub hub = hubService.updateHub(hubUpdateRequest, hubID);
            if (hub != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Update hub successfully", hub));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Update hub failed. Hub is null", null));
        } catch (Exception e) {
            log.error("Error updating hub", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Update hub failed", null));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{hub-id}")
    public ResponseEntity<ObjectResponse> deleteHubByID(@PathVariable("hub-id") int hubID) {
        try {
            Hub hub = hubService.findById(hubID);
            if(hub != null) {
                hub.setDeleted(true);
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Delete hub successfully", hubService.save(hub)));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Delete hub failed", null));
        } catch (Exception e) {
            log.error("Error deleting hub", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Delete hub failed", null));
        }
    }

}
