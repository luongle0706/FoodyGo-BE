package com.foodygo.controller;

import com.foodygo.dto.request.BuildingCreateRequest;
import com.foodygo.dto.request.BuildingUpdateRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.entity.Building;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.service.BuildingService;
import com.foodygo.service.HubService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/sdw391/v1/building")
public class BuildingController {

    private final BuildingService buildingService;
    private final HubService hubService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all")
    public ResponseEntity<ObjectResponse> getAllBuildings() {
        return !buildingService.findAll().isEmpty() ?
            ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all buildings successfully", buildingService.findAll())) :
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Get all buildings failed", null));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all-active")
    public ResponseEntity<ObjectResponse> getAllHubsActive() {
        return !buildingService.getBuildingsActive().isEmpty() ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all buildings active successfully", buildingService.getBuildingsActive())) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Get all buildings active failed", null));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/undelete/{building-id}")
    public ResponseEntity<ObjectResponse> unDeleteHubByID(@PathVariable("building-id") int buildingID) {
        return buildingService.undeleteBuilding(buildingID) != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Undelete building successfully", buildingService.findById(buildingID))) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Undelete building failed", null));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get/{building-id}")
    public ResponseEntity<ObjectResponse> getBuildingByID(@PathVariable("building-id") int buildingID) {
        return buildingService.findById(buildingID) != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get building by ID successfully", buildingService.findById(buildingID))) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Get building by ID failed", null));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<ObjectResponse> createBuilding(@Valid @RequestBody BuildingCreateRequest buildingCreateRequest) {
        try {
            Building building = buildingService.createBuilding(buildingCreateRequest);
            if (building != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Create building successfully", building));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Create building failed. Building is null", null));
        } catch (ElementNotFoundException e) {
            log.error("Error while creating building", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Create building failed. Hub not found", null));
        } catch (Exception e) {
            log.error("Error while creating building", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Create building failed", null));
        }
    }

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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Update building failed. Hub not found", null));
        } catch (Exception e) {
            log.error("Error while updating building", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Update building failed", null));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{building-id}")
    public ResponseEntity<ObjectResponse> deleteHubByID(@PathVariable("building-id") int buildingID) {
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
