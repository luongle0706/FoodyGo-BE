package com.foodygo.controller;

import com.foodygo.dto.request.HubCreateRequest;
import com.foodygo.dto.request.HubUpdateRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.entity.Building;
import com.foodygo.entity.Hub;
import com.foodygo.entity.Order;
import com.foodygo.exception.ElementNotFoundException;
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

    // lấy tất cả các hubs
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all")
    public ResponseEntity<ObjectResponse> getAllHubs() {
        List<Hub> results = hubService.findAll();
        return !results.isEmpty() ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all hubs successfully", results)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Get all hubs failed", null));
    }

    // lấy tất cả các buildings từ hub id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all-buildings/{hub-id}")
    public ResponseEntity<ObjectResponse> getBuildingsByHubID(@PathVariable("hub-id") int hubID) {
        List<Building> results = hubService.getBuildingsByHubID(hubID);
        return results != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all buildings by hub ID successfully", results)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Get all buildings by hub ID failed", null));
    }

    // lấy tất cả các orders từ hub id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all-orders/{hub-id}")
    public ResponseEntity<ObjectResponse> getOrdersByHubID(@PathVariable("hub-id") int hubID) {
        List<Order> results = hubService.getOrdersByHubID(hubID);
        return results != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all orders by hub ID successfully", results)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Get all orders by hub ID failed", null));
    }

    // lấy hub từ order id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-hub/{order-id}")
    public ResponseEntity<ObjectResponse> getHubByOrderID(@PathVariable("order-id") int orderID) {
        Hub results = hubService.getHubByOrderID(orderID);
        return results != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get hub by order ID successfully", results)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Get hub by order ID failed", null));
    }

    // lấy tất cả các hubs chưa bị xóa
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all-active")
    public ResponseEntity<ObjectResponse> getAllHubsActive() {
        List<Hub> results = hubService.getHubsActive();
        return !results.isEmpty() ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all hubs active successfully", results)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Get all hubs active failed", null));
    }

    // khôi phục lại hub đó
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/undelete/{hub-id}")
    public ResponseEntity<ObjectResponse> unDeleteHubByID(@PathVariable("hub-id") int hubID) {
        return hubService.undeleteHub(hubID) != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Undelete hub successfully", hubService.findById(hubID))) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Undelete hub failed", null));
    }

    // lấy ra hub bằng id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get/{hub-id}")
    public ResponseEntity<ObjectResponse> getHubByID(@PathVariable("hub-id") int hubID) {
        Hub hub = hubService.findById(hubID);
        return hub != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get hub by ID successfully", hub)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Get hub by ID failed", null));
    }

    // tạo ra hub
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<ObjectResponse> createHub(@Valid @RequestBody HubCreateRequest hubCreateRequest) {
        try {
            Hub hub = hubService.createHub(hubCreateRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Create hub successfully", hub));
        } catch (Exception e) {
            log.error("Error creating hub", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Create hub failed", null));
        }
    }

    // update hub bằng id
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update/{hub-id}")
    public ResponseEntity<ObjectResponse> updateHub(@PathVariable("hub-id") int hubID, @RequestBody HubUpdateRequest hubUpdateRequest) {
        try {
            Hub hub = hubService.updateHub(hubUpdateRequest, hubID);
            if (hub != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Update hub successfully", hub));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Update hub failed. Hub is null", null));
        } catch (ElementNotFoundException e) {
            log.error("Error while updating hub", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Update hub failed. Hub not found", null));
        } catch (Exception e) {
            log.error("Error updating hub", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Update hub failed", null));
        }
    }

    // xóa hub, tức set deleted = true
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
