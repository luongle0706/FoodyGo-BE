package com.foodygo.controller;

import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.entity.Building;
import com.foodygo.service.BuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sdw391/v1/building")
public class BuildingController {

    private final BuildingService buildingService;

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
    @PostMapping("/undelete/{hub-id}")
    public ResponseEntity<ObjectResponse> unDeleteHubByID(@PathVariable("hub-id") int hubID) {
        return buildingService.undeleteBuilding(hubID) != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Undelete hub successfully", buildingService.findById(hubID))) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Undelete hub failed", null));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get/{building-id}")
    public ResponseEntity<ObjectResponse> getBuildingByID(@PathVariable("building-id") int buildingID) {
        return buildingService.findById(buildingID) != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get building by ID successfully", buildingService.findById(buildingID))) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Get building by ID failed", null));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{building-id}")
    public ResponseEntity<ObjectResponse> deleteHubByID(@PathVariable("building-id") int buildingID) {
        Building building = buildingService.findById(buildingID);
        if(building != null) {
            building.setDeleted(true);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Delete building successfully", buildingService.save(building)));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Delete building failed", null));
    }

}
