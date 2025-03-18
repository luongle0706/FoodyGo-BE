package com.foodygo.controller;

import com.foodygo.dto.AddonSectionDTO;
import com.foodygo.dto.internal.PagingRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.service.spec.AddonItemService;
import com.foodygo.service.spec.AddonSectionService;
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
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/addon-sections")
@RequiredArgsConstructor
@Tag(name = "Addon Section")
public class AddonSectionController {

    private final AddonSectionService addonSectionService;
    private final AddonItemService addonItemService;

    @Value("${application.default-page-size}")
    private int defaultPageSize;

    @GetMapping()
    @Operation(summary = "Get Addon Section", description = "Retrieve a paginated list of all add-on section. Supports sorting and pagination.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Addon section found"),
            @ApiResponse(responseCode = "400", description = "Invalid addon section request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Addon section not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<MappingJacksonValue> getAllAddonSection(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String params,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) Map<String, String> filters
    ) {
        filters.remove("pageNo");
        filters.remove("pageSize");
        filters.remove("sortBy");
        filters.remove("filters");
        return ResponseEntity
                .status(OK)
                .body(
                        addonSectionService.getAddonSections(PagingRequest.builder()
                                .pageNo(pageNo)
                                .pageSize(pageSize)
                                .params(params)
                                .sortBy(sortBy)
                                .filters(filters)
                                .build())
                );
    }

    @GetMapping("/{addonSectionId}")
    @Operation(summary = "Get Addon Section by ID", description = "Retrieve details of an addon section by its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Addon section found"),
            @ApiResponse(responseCode = "400", description = "Invalid addon section request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Addon section not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> getAddonSectionById(@PathVariable Integer addonSectionId) {
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(OK.toString())
                                .message("Get addon section by id")
                                .data(addonSectionService.getAddonSectionDTOById(addonSectionId))
                                .build()
                );
    }

    @PostMapping
    @Operation(summary = "Create Addon Section", description = "Create a new addon section using the provided data.")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN','SELLER')")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Addon section created"),
            @ApiResponse(responseCode = "400", description = "Invalid addon section body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> createAddonSection(
            @RequestBody AddonSectionDTO.CreateRequest request
    ) {
        return ResponseEntity
                .status(CREATED)
                .body(
                        ObjectResponse.builder()
                                .status(CREATED.toString())
                                .message("Create addon section")
                                .data(addonSectionService.createAddonSectionDTO(request))
                                .build()
                );
    }

    @PutMapping
    @Operation(summary = "Update Addon Section", description = "Update an existing addon section with the provided data.")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN','SELLER')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Addon section updated"),
            @ApiResponse(responseCode = "400", description = "Invalid addon section body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Addon section not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> updateAddonSection(
            @RequestBody AddonSectionDTO.UpdateRequest request
    ) {
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(OK.toString())
                                .message("Updated addon section")
                                .data(addonSectionService.updateAddonSectionDTO(request))
                                .build()
                );
    }

    @DeleteMapping("/{addonSectionId}")
    @Operation(summary = "Delete Addon Section", description = "Delete an addon section by its unique ID.")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN','SELLER')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Addon section availability updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Addon section not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> deleteAddonSection(
            @PathVariable Integer addonSectionId
    ) {
        addonSectionService.deleteAddonSection(addonSectionId);
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(OK.toString())
                                .message("Deleted addon section")
                                .build()
                );
    }

    @GetMapping("/{sectionId}/addon-items")
    @Operation(summary = "Get Addon Items by Section", description = "Retrieve a paginated list of addon items by the specified section ID. Supports sorting and pagination.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Addon items found"),
            @ApiResponse(responseCode = "400", description = "Invalid addon item request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Addon section not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> getAddonItemsBySection(
            @PathVariable Integer sectionId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending
    ) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize != null ? pageSize : defaultPageSize, sort);
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(OK.toString())
                                .message("Get all categories")
                                .data(addonItemService.getAddonItemsBySectionId(sectionId, pageable))
                                .build()
                );
    }

}
