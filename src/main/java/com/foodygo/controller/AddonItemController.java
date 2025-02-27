package com.foodygo.controller;

import com.foodygo.dto.AddonItemDTO;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.service.AddonItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/addon-items")
@RequiredArgsConstructor
@Tag(name = "Addon Item")
public class AddonItemController {

    private final AddonItemService addonItemService;

    @GetMapping("/{addonItemId}")
    @Operation(summary = "Get Addon Item by ID", description = "Retrieve details of an addon item by its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Addon item found"),
            @ApiResponse(responseCode = "400", description = "Invalid addon item request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Addon item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> getAddonItemById(
            @PathVariable Integer addonItemId
    ) {
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(OK.toString())
                                .message("Get addon item by id")
                                .data(addonItemService.getAddonItemDTOById(addonItemId))
                                .build()
                );
    }

    @PostMapping
    @Operation(summary = "Create Addon Item", description = "Create a new addon item using the provided data.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Addon item created"),
            @ApiResponse(responseCode = "400", description = "Invalid addon item body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> createAddonItem(
            @RequestBody AddonItemDTO.CreateRequest request
    ) {
        return ResponseEntity
                .status(CREATED)
                .body(
                        ObjectResponse.builder()
                                .status(CREATED.toString())
                                .message("Create addon item")
                                .data(addonItemService.createAddonItemDTO(request))
                                .build()
                );
    }

    @PutMapping
    @Operation(summary = "Update Addon Item", description = "Update an existing addon item with the provided data.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Addon item updated"),
            @ApiResponse(responseCode = "400", description = "Invalid addon item body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Addon item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> updateAddonItem(
            @RequestBody AddonItemDTO.UpdateRequest request
    ) {
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(OK.toString())
                                .message("Update addon item")
                                .data(addonItemService.updateAddonItemDTO(request))
                                .build()
                );
    }

    @DeleteMapping("/{addonItemId}")
    @Operation(summary = "Delete Addon Item", description = "Delete an addon item by its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Addon item availability updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Addon item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> deleteAddonItem(
            @PathVariable Integer addonItemId
    ) {
        addonItemService.deleteAddonItem(addonItemId);
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(OK.toString())
                                .status("Delete addon item")
                                .build()
                );
    }
}
