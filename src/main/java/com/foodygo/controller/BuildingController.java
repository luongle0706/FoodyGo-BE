package com.foodygo.controller;

import com.foodygo.dto.BuildingDTO;
import com.foodygo.dto.HubDTO;
import com.foodygo.dto.request.BuildingCreateRequest;
import com.foodygo.dto.request.BuildingUpdateRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.dto.response.PagingResponse;
import com.foodygo.entity.Building;
import com.foodygo.exception.ElementExistException;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.exception.UnchangedStateException;
import com.foodygo.service.spec.BuildingService;
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
@RequestMapping("/api/v1/buildings")
@Tag(name = "Building", description = "Operations related to building management")
public class BuildingController {

    private final BuildingService buildingService;

    @Value("${application.default-current-page}")
    private int defaultCurrentPage;

    @Value("${application.default-page-size}")
    private int defaultPageSize;

//     /**
//     * Method get all buildings or all buildings by status
//     *
//     * @param currentPage currentOfThePage
//     * @param pageSize numberOfElement
//     * @return list or empty
//     */
//    @Operation(summary = "Get all buildings", description = "Retrieves all buildings, with optional pagination and filtering by status")
//    @PreAuthorize("hasRole('MANAGER') or hasRole('STAFF')")
//    @GetMapping("")
//    public ResponseEntity<PagingResponse> getAllBuildings(
//            @RequestParam(value = "currentPage", required = false) Integer currentPage,
//            @RequestParam(value = "pageSize", required = false) Integer pageSize,
//            @RequestParam(value = "status", required = false) String status) {
//
//        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
//        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
//        PagingResponse results = (status != null && status.equals("active"))
//                ? buildingService.getBuildingsActive(resolvedCurrentPage, resolvedPageSize)
//                : buildingService.getAllBuildings(resolvedCurrentPage, resolvedPageSize);
//        List<?> data = (List<?>) results.getData();
//        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
//    }

    /**
     * Method get all buildings
     *
     * @param currentPage currentOfThePage
     * @param pageSize numberOfElement
     * @return list or empty
     */
    @Operation(summary = "Get all buildings", description = "Retrieves all buildings, with optional pagination")
    @PreAuthorize("hasRole('MANAGER') or hasRole('STAFF')")
    @GetMapping("")
    public ResponseEntity<PagingResponse> getAllBuildings(@RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                          @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
        PagingResponse results = buildingService.getAllBuildings(resolvedCurrentPage, resolvedPageSize);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

    /**
     * Method get all buildings have status active
     *
     * @param currentPage currentOfThePage
     * @param pageSize numberOfElement
     * @return list or empty
     */
    @Operation(summary = "Get all buildings active", description = "Retrieves all buildings have status active, with optional pagination")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('STAFF')")
    @GetMapping("/active")
    public ResponseEntity<PagingResponse> getAllBuildingsActive(@RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                                @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
        PagingResponse results = buildingService.getBuildingsActive(resolvedCurrentPage, resolvedPageSize);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

    /**
     * Method search buildings with name and sortBy
     *
     * @param currentPage currentOfThePage
     * @param pageSize    numberOfElement
     * @param name name of building to search
     * @param sortBy sortBy with "nameasc, namedesc"
     * @return list or empty
     */
    @Operation(summary = "Search buildings", description = "Retrieves all buildings are filtered by name and sortBy")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<PagingResponse> searchBuildings(@RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                         @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                         @RequestParam(value = "name", required = false, defaultValue = "") String name,
                                                         @RequestParam(value = "sortBy", required = false) String sortBy) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;

        PagingResponse results = buildingService.searchBuildings(resolvedCurrentPage, resolvedPageSize, name, sortBy);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

    /**
     * Method get all buildings non paging
     *
     * @return list or empty
     */
    @Operation(summary = "Get all buildings non paging", description = "Retrieves all buildings, without optional pagination")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER')")
    @GetMapping("/non-paging")
    public ResponseEntity<ObjectResponse> getAllBuildingsNonPaging() {
        List<BuildingDTO> results = buildingService.getBuildings();
        return !results.isEmpty() ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "get all buildings non paging successfully", results)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "get all buildings non paging failed", results));
    }

    /**
     * Method get hub by building id
     *
     * @param buildingID idOfBuilding
     * @return hub or null
     */
    @Operation(summary = "Get hub by building id", description = "Get hub by building id")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('STAFF')")
    @GetMapping("/{building-id}/hub")
    public ResponseEntity<ObjectResponse> getHubByBuildingID(@PathVariable("building-id") int buildingID) {
        HubDTO hub = buildingService.getHubByBuildingID(buildingID);
        return hub != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all hub by building ID successfully", hub)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get all hub by building ID failed", null));
    }

    /**
     * Method get all customers by building id
     *
     * @param buildingID idOfBuilding
     * @param currentPage currentOfThePage
     * @param pageSize numberOfElement
     * @return list or empty
     */
    @Operation(summary = "Get all customers by building id", description = "Retrieves all customers by building id, with optional pagination")
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/{building-id}/customers")
    public ResponseEntity<PagingResponse> getCustomersByBuildingID(@PathVariable("building-id") int buildingID,
                                                                   @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
        PagingResponse results = buildingService.getCustomersByBuildingID(buildingID, resolvedCurrentPage, resolvedPageSize);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

    /**
     * Method restore building by building id and set deleted = false
     *
     * @param buildingID idOfBuilding
     * @return list or empty
     */
    @Operation(summary = "Restore building by building id", description = "Restore building by building id and set deleted = false")
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/{building-id}/restore")
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

    /**
     * Method get building by id
     *
     * @param buildingID idOfBuilding
     * @return list or empty
     */
    @Operation(summary = "Get building by id", description = "Get building by id")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('STAFF')")
    @GetMapping("/{building-id}")
    public ResponseEntity<ObjectResponse> getBuildingByID(@PathVariable("building-id") int buildingID) {
        Building building = buildingService.findById(buildingID);
        return building != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get building by ID successfully", building)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get building by ID failed", null));
    }

    /**
     * Method create building
     *
     * @param buildingCreateRequest param basic info for saving db
     * @return building or null
     */
    @Operation(summary = "Create building", description = "Create building")
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("")
    public ResponseEntity<ObjectResponse> createBuilding(@Valid @RequestBody BuildingCreateRequest buildingCreateRequest) {
        try {
            BuildingDTO building = buildingService.createBuilding(buildingCreateRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Create building successfully", building));
        } catch (ElementExistException e) {
            log.error("Error while creating building", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Create building failed." + e.getMessage(), null));
        } catch (ElementNotFoundException e) {
            log.error("Error while creating building", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Create building failed. " + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error while creating building", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Create building failed", null));
        }
    }

    /**
     * Method update building by building id
     *
     * @param buildingID idOfBuilding
     * @param buildingUpdateRequest param basic info for saving db
     * @return building or null
     */
    @Operation(summary = "Update building by building id", description = "Update building by building id")
    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{building-id}")
    public ResponseEntity<ObjectResponse> updateBuilding(@Valid @RequestBody BuildingUpdateRequest buildingUpdateRequest, @PathVariable("building-id") int buildingID) {
        try {
            BuildingDTO building = buildingService.updateBuilding(buildingUpdateRequest, buildingID);
            if (building != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Update building successfully", building));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Update building failed. Building is null", null));
        } catch (ElementExistException e) {
            log.error("Error while updating building", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Update building failed. " + e.getMessage(), null));
        } catch (ElementNotFoundException e) {
            log.error("Error while updating building", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Update building failed. " + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error while updating building", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Update building failed", null));
        }
    }

    /**
     * Method delete building by building id and set deleted = true
     *
     * @param buildingID idOfBuilding
     * @return building or null
     */
    @Operation(summary = "Delete building by building id", description = "Delete building by building id and set deleted = true")
    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{building-id}")
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
