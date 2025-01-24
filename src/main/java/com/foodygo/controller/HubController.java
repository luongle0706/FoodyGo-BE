package com.foodygo.controller;

import com.foodygo.dto.CustomerDTO;
import com.foodygo.dto.HubDTO;
import com.foodygo.dto.request.HubCreateRequest;
import com.foodygo.dto.request.HubUpdateRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.dto.response.PagingResponse;
import com.foodygo.entity.Building;
import com.foodygo.entity.Hub;
import com.foodygo.entity.Order;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.exception.UnchangedStateException;
import com.foodygo.service.HubService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hub")
public class HubController {

    private final HubService hubService;

    @Value("${application.default-current-page}")
    private int defaultCurrentPage;

    @Value("${application.default-page-size}")
    private int defaultPageSize;

    // lấy tất cả các hubs
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all")
    public ResponseEntity<PagingResponse> getAllHubs(@RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                     @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
        PagingResponse results = hubService.getHubsPaging(resolvedCurrentPage, resolvedPageSize);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

    // lấy tất cả các buildings từ hub id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all-buildings/{hub-id}")
    public ResponseEntity<PagingResponse> getBuildingsByHubID(@PathVariable("hub-id") int hubID,
                                                              @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                              @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
        PagingResponse results = hubService.getBuildingsByHubID(hubID, resolvedCurrentPage, resolvedPageSize);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

    // lấy tất cả các orders từ hub id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all-orders/{hub-id}")
    public ResponseEntity<ObjectResponse> getOrdersByHubID(@PathVariable("hub-id") int hubID) {
        List<Order> results = hubService.getOrdersByHubID(hubID);
        return results != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all orders by hub ID successfully", results)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get all orders by hub ID failed", null));
    }

    // lấy hub từ order id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-hub/{order-id}")
    public ResponseEntity<ObjectResponse> getHubByOrderID(@PathVariable("order-id") int orderID) {
        HubDTO results = hubService.getHubByOrderID(orderID);
        return results != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get hub by order ID successfully", results)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get hub by order ID failed", null));
    }

    // lấy tất cả các hubs chưa bị xóa
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all-active")
    public ResponseEntity<PagingResponse> getAllHubsActive(@RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                           @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
        PagingResponse results = hubService.getHubsActive(resolvedCurrentPage, resolvedPageSize);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

    // khôi phục lại hub đó
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/undelete/{hub-id}")
    public ResponseEntity<ObjectResponse> unDeleteHubByID(@PathVariable("hub-id") int hubID) {
        try {
            HubDTO hub = hubService.undeleteHub(hubID);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Undelete hub successfully", hub));
        } catch (ElementNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Undelete hub failed. " + e.getMessage(), null));
        } catch (UnchangedStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Undelete hub failed. " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Undelete hub failed", null));
        }
    }

    // lấy ra hub bằng id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get/{hub-id}")
    public ResponseEntity<ObjectResponse> getHubByID(@PathVariable("hub-id") int hubID) {
        Hub hub = hubService.findById(hubID);
        return hub != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get hub by ID successfully", hub)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get hub by ID failed", null));
    }

    // tạo ra hub
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<ObjectResponse> createHub(@Valid @RequestBody HubCreateRequest hubCreateRequest) {
        try {
            HubDTO hub = hubService.createHub(hubCreateRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Create hub successfully", hub));
        } catch (Exception e) {
            log.error("Error creating hub", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Create hub failed", null));
        }
    }

    // update hub bằng id
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update/{hub-id}")
    public ResponseEntity<ObjectResponse> updateHub(@PathVariable("hub-id") int hubID, @RequestBody HubUpdateRequest hubUpdateRequest) {
        try {
            HubDTO hub = hubService.updateHub(hubUpdateRequest, hubID);
            if (hub != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Update hub successfully", hub));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Update hub failed. Hub is null", null));
        } catch (ElementNotFoundException e) {
            log.error("Error while updating hub", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Update hub failed. Hub not found", null));
        } catch (Exception e) {
            log.error("Error updating hub", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Update hub failed", null));
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Delete hub failed", null));
        } catch (Exception e) {
            log.error("Error deleting hub", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Delete hub failed", null));
        }
    }

}
