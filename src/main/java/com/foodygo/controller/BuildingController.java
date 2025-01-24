package com.foodygo.controller;

import com.foodygo.dto.BuildingDTO;
import com.foodygo.dto.HubDTO;
import com.foodygo.dto.request.BuildingCreateRequest;
import com.foodygo.dto.request.BuildingUpdateRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.dto.response.PagingResponse;
import com.foodygo.entity.Building;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.exception.UnchangedStateException;
import com.foodygo.service.BuildingService;
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
@RequestMapping("/api/v1/building")
public class BuildingController {

    private final BuildingService buildingService;

    @Value("${application.default-current-page}")
    private int defaultCurrentPage;

    @Value("${application.default-page-size}")
    private int defaultPageSize;

    // lấy tất cả buildings
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all")
    public ResponseEntity<PagingResponse> getAllBuildings(@RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                          @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
        PagingResponse results = buildingService.getAllBuildings(resolvedCurrentPage, resolvedPageSize);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

    // lấy tất cả building chưa xóa
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all-active")
    public ResponseEntity<PagingResponse> getAllBuildingsActive(@RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                                @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
        PagingResponse results = buildingService.getBuildingsActive(resolvedCurrentPage, resolvedPageSize);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

    // lấy hub theo building id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-hub/{building-id}")
    public ResponseEntity<ObjectResponse> getHubByBuildingID(@PathVariable("building-id") int buildingID) {
        HubDTO hub = buildingService.getHubByBuildingID(buildingID);
        return hub != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all hub by building ID successfully", hub)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get all hub by building ID failed", null));
    }

    // lấy tất cả customer trong building
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all-customers/{building-id}")
    public ResponseEntity<PagingResponse> getCustomersByBuildingID(@PathVariable("building-id") int buildingID,
                                                                   @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
        PagingResponse results = buildingService.getCustomersByBuildingID(buildingID, resolvedCurrentPage, resolvedPageSize);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

    // khôi phục building
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/undelete/{building-id}")
    public ResponseEntity<ObjectResponse> unDeleteBuildingByID(@PathVariable("building-id") int buildingID) {
        try {
            BuildingDTO building = buildingService.undeleteBuilding(buildingID);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Undelete building successfully", building));
        } catch (ElementNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Undelete building failed. " + e.getMessage(), null));
        } catch (UnchangedStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Undelete building failed. " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Undelete building failed", null));
        }
    }

    // get building by id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get/{building-id}")
    public ResponseEntity<ObjectResponse> getBuildingByID(@PathVariable("building-id") int buildingID) {
        Building building = buildingService.findById(buildingID);
        return building != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get building by ID successfully", building)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get building by ID failed", null));
    }

    // create building
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<ObjectResponse> createBuilding(@Valid @RequestBody BuildingCreateRequest buildingCreateRequest) {
        try {
            BuildingDTO building = buildingService.createBuilding(buildingCreateRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Create building successfully", building));
        } catch (ElementNotFoundException e) {
            log.error("Error while creating building", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Create building failed. Building not found", null));
        } catch (Exception e) {
            log.error("Error while creating building", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Create building failed", null));
        }
    }

    // update building theo id
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update/{building-id}")
    public ResponseEntity<ObjectResponse> updateBuilding(@Valid @RequestBody BuildingUpdateRequest buildingUpdateRequest, @PathVariable("building-id") int buildingID) {
        try {
            BuildingDTO building = buildingService.updateBuilding(buildingUpdateRequest, buildingID);
            if (building != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Update building successfully", building));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Update building failed. Building is null", null));
        } catch (ElementNotFoundException e) {
            log.error("Error while updating building", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Update building failed. Building not found", null));
        } catch (Exception e) {
            log.error("Error while updating building", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Update building failed", null));
        }
    }

    // xóa building
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{building-id}")
    public ResponseEntity<ObjectResponse> deleteBuildingByID(@PathVariable("building-id") int buildingID) {
        try {
            BuildingDTO building = buildingService.deleteBuilding(buildingID);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Delete building successfully", building));
        } catch (ElementNotFoundException e) {
            log.error("Error while deleting building", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Delete building failed. " + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error while deleting building", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Delete building failed", null));
        }
    }

}
