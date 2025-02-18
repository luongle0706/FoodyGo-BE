package com.foodygo.controller;

import com.foodygo.dto.HubDTO;
import com.foodygo.dto.request.HubCreateRequest;
import com.foodygo.dto.request.HubUpdateRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.dto.response.PagingResponse;
import com.foodygo.entity.Hub;
import com.foodygo.entity.Order;
import com.foodygo.exception.ElementExistException;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.exception.UnchangedStateException;
import com.foodygo.service.HubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v1/hubs")
@Tag(name = "Hub", description = "Operations related to hub management")
public class HubController {

    private final HubService hubService;

    @Value("${application.default-current-page}")
    private int defaultCurrentPage;

    @Value("${application.default-page-size}")
    private int defaultPageSize;

    /**
     * Method get all hubs
     *
     * @param currentPage currentOfThePage
     * @param pageSize numberOfElement
     * @return list or empty
     */
    @Operation(summary = "Get all hubs", description = "Retrieves all hubs, with optional pagination")
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/get-all")
    public ResponseEntity<PagingResponse> getAllHubs(
            @RequestParam(value = "currentPage", required = false) Integer currentPage,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "status", required = false) String status) {

        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
        PagingResponse results = (status != null && status.equals("active"))
                ? hubService.getHubsActive(resolvedCurrentPage, resolvedPageSize)
                : hubService.getHubsPaging(resolvedCurrentPage, resolvedPageSize);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

//    /**
//     * Method get all hubs
//     *
//     * @param currentPage currentOfThePage
//     * @param pageSize numberOfElement
//     * @return list or empty
//     */
//    @Operation(summary = "Get all hubs", description = "Retrieves all hubs, with optional pagination")
//    @PreAuthorize("hasRole('MANAGER')")
//    @GetMapping("/get-all")
//    public ResponseEntity<PagingResponse> getAllHubs(@RequestParam(value = "currentPage", required = false) Integer currentPage,
//                                                     @RequestParam(value = "pageSize", required = false) Integer pageSize) {
//        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
//        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
//        PagingResponse results = hubService.getHubsPaging(resolvedCurrentPage, resolvedPageSize);
//        List<?> data = (List<?>) results.getData();
//        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
//    }

//    /**
//     * Method get all hubs have status is active
//     *
//     * @param currentPage currentOfThePage
//     * @param pageSize numberOfElement
//     * @return list or empty
//     */
//    @Operation(summary = "Get all hubs active", description = "Retrieves all hubs have status is active")
//    @PreAuthorize("hasRole('USER') or hasRole('MANAGER')")
//    @GetMapping("/get-all-active")
//    public ResponseEntity<PagingResponse> getAllHubsActive(@RequestParam(value = "currentPage", required = false) Integer currentPage,
//                                                           @RequestParam(value = "pageSize", required = false) Integer pageSize) {
//        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
//        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
//        PagingResponse results = hubService.getHubsActive(resolvedCurrentPage, resolvedPageSize);
//        List<?> data = (List<?>) results.getData();
//        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
//    }


    /**
     * Method get all buildings by hub id
     *
     * @param hubID idOfHub
     * @param currentPage currentOfThePage
     * @param pageSize numberOfElement
     * @return list or empty
     */
    @Operation(summary = "Get all buildings by hub id", description = "Retrieves all buildings by hub id, with optional pagination")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('STAFF')")
    @GetMapping("/{hub-id}/buildings")
    public ResponseEntity<PagingResponse> getBuildingsByHubID(@PathVariable("hub-id") int hubID,
                                                              @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                              @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
        PagingResponse results = hubService.getBuildingsByHubID(hubID, resolvedCurrentPage, resolvedPageSize);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

    /**
     * Method get all orders by hub id
     *
     * @param hubID idOfHub
     * @return list or empty
     */
    @Operation(summary = "Get all orders by hub id", description = "Retrieves all orders by hub id")
    @PreAuthorize("hasRole('MANAGER') or hasRole('STAFF')")
    @GetMapping("/{hub-id}/orders")
    public ResponseEntity<ObjectResponse> getOrdersByHubID(@PathVariable("hub-id") int hubID) {
        List<Order> results = hubService.getOrdersByHubID(hubID);
        return results != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all orders by hub ID successfully", results)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get all orders by hub ID failed", null));
    }

//    // lấy hub từ order id
//    /**
//     * Method get all orders by hub id
//     *
//     * @param hubID idOfHub
//     * @return list or empty
//     */
//    @Operation(summary = "Get all orders by hub id", description = "Retrieves all orders by hub id")
//    @PreAuthorize("hasRole('USER') or hasRole('MANAGER')")
//    @GetMapping("/get-hub/{order-id}")
//    public ResponseEntity<ObjectResponse> getHubByOrderID(@PathVariable("order-id") int orderID) {
//        HubDTO results = hubService.getHubByOrderID(orderID);
//        return results != null ?
//                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get hub by order ID successfully", results)) :
//                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get hub by order ID failed", null));
//    }

    /**
     * Method restore hub by hub id set delete = false
     *
     * @param hubID idOfHub
     * @return hub or null
     */
    @Operation(summary = "Restore hubs", description = "Restore hub by hub id set delete = false")
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/{hub-id}/restore")
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

    /**
     * Method get hub by id
     *
     * @param hubID idOfHub
     * @return hub or null
     */
    @Operation(summary = "Get hub by id", description = "Get hub by id")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('STAFF')")
    @GetMapping("/{hub-id}")
    public ResponseEntity<ObjectResponse> getHubByID(@PathVariable("hub-id") int hubID) {
        Hub hub = hubService.findById(hubID);
        return hub != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get hub by ID successfully", hub)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get hub by ID failed", null));
    }

    /**
     * Method create hub
     *
     * @param hubCreateRequest param basic for hub
     * @return hub or null
     */
    @Operation(summary = "Create hub", description = "Create hub")
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("")
    public ResponseEntity<ObjectResponse> createHub(@Valid @RequestBody HubCreateRequest hubCreateRequest) {
        try {
            HubDTO hub = hubService.createHub(hubCreateRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Create hub successfully", hub));
        } catch (ElementExistException e) {
            log.error("Error creating hub", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Create hub failed. " + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error creating hub", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Create hub failed", null));
        }
    }

    /**
     * Method update hub by id
     *
     * @param hubUpdateRequest param basic update for hub
     * @return hub or null
     */
    @Operation(summary = "Update hub by id", description = "Update hub by id")
    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{hub-id}")
    public ResponseEntity<ObjectResponse> updateHub(@PathVariable("hub-id") int hubID, @RequestBody HubUpdateRequest hubUpdateRequest) {
        try {
            HubDTO hub = hubService.updateHub(hubUpdateRequest, hubID);
            if (hub != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Update hub successfully", hub));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Update hub failed. Hub is null", null));
        } catch (ElementExistException e) {
            log.error("Error while updating hub", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Update hub failed. " + e.getMessage(), null));
        } catch (ElementNotFoundException e) {
            log.error("Error while updating hub", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Update hub failed. Hub not found", null));
        } catch (Exception e) {
            log.error("Error updating hub", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Update hub failed", null));
        }
    }

    /**
     * Method delete hub set deleted = true
     *
     * @param hubID idOfHub
     * @return hub or null
     */
    @Operation(summary = "Delete hub by id", description = "Delete hub by id and set deleted = true")
    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{hub-id}")
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
