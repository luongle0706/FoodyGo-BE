package com.foodygo.controller;

import com.foodygo.dto.request.BuildingCreateRequest;
import com.foodygo.dto.request.BuildingUpdateRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.entity.Building;
import com.foodygo.entity.Customer;
import com.foodygo.entity.Hub;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.service.BuildingService;
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
@RequestMapping("/sdw391/v1/building")
public class BuildingController {

    private final BuildingService buildingService;

    // lấy tất cả buildings
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all")
    public ResponseEntity<ObjectResponse> getAllBuildings() {
        List<Building> buildings = buildingService.findAll();
        return !buildings.isEmpty() ?
            ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all buildings successfully", buildings)) :
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Get all buildings failed", null));
    }

    // lấy hub theo building id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all-hub/{building-id}")
    public ResponseEntity<ObjectResponse> getHubByBuildingID(@PathVariable("building-id") int buildingID) {
        Hub hub = buildingService.getHubByBuildingID(buildingID);
        return hub != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all hub by building ID successfully", hub)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Get all hub by building ID failed", null));
    }

    // lấy tất cả customer trong building
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all-customers/{building-id}")
    public ResponseEntity<ObjectResponse> getCustomersByBuildingID(@PathVariable("building-id") int buildingID) {
        List<Customer> results = buildingService.getCustomersByBuildingID(buildingID);
        return results != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all customer by building ID successfully", results)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Get all customer by building ID failed", null));
    }

    // lấy tất cả building chưa xóa
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all-active")
    public ResponseEntity<ObjectResponse> getAllBuildingsActive() {
        List<Building> buildings = buildingService.getBuildingsActive();
        return !buildings.isEmpty() ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all buildings active successfully", buildings)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Get all buildings active failed", null));
    }

    // khôi phục building
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/undelete/{building-id}")
    public ResponseEntity<ObjectResponse> unDeleteBuildingByID(@PathVariable("building-id") int buildingID) {
        return buildingService.undeleteBuilding(buildingID) != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Undelete building successfully", buildingService.findById(buildingID))) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Undelete building failed", null));
    }

    // get building by id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get/{building-id}")
    public ResponseEntity<ObjectResponse> getBuildingByID(@PathVariable("building-id") int buildingID) {
        Building building = buildingService.findById(buildingID);
        return building != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get building by ID successfully", building)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Get building by ID failed", null));
    }

    // create building
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<ObjectResponse> createBuilding(@Valid @RequestBody BuildingCreateRequest buildingCreateRequest) {
        try {
            Building building = buildingService.createBuilding(buildingCreateRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Create building successfully", building));
        } catch (ElementNotFoundException e) {
            log.error("Error while creating building", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Create building failed. Building not found", null));
        } catch (Exception e) {
            log.error("Error while creating building", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Create building failed", null));
        }
    }

    // update building theo id
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update/{building-id}")
    public ResponseEntity<ObjectResponse> updateBuilding(@Valid @RequestBody BuildingUpdateRequest buildingUpdateRequest, @PathVariable("building-id") int buildingID) {
        try {
            Building building = buildingService.updateBuilding(buildingUpdateRequest, buildingID);
            if (building != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Update building successfully", buildingService.save(building)));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Update building failed. Building is null", null));
        } catch (ElementNotFoundException e) {
            log.error("Error while updating building", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Update building failed. Building not found", null));
        } catch (Exception e) {
            log.error("Error while updating building", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Update building failed", null));
        }
    }

    // xóa building
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{building-id}")
    public ResponseEntity<ObjectResponse> deleteBuildingByID(@PathVariable("building-id") int buildingID) {
        try {
            Building building = buildingService.findById(buildingID);
            if(building != null) {
                building.setDeleted(true);
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Delete building successfully", buildingService.save(building)));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Delete building failed", null));
        } catch (Exception e) {
            log.error("Error while deleting building", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Delete building failed", null));
        }
    }

}
