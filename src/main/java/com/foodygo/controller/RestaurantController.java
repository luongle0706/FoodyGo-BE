package com.foodygo.controller;

import com.foodygo.dto.RestaurantDTO;
import com.foodygo.dto.internal.PagingRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.service.spec.CategoryService;
import com.foodygo.service.spec.ProductService;
import com.foodygo.service.spec.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/restaurants")
@Tag(name = "Restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final ProductService productService;
    private final CategoryService categoryService;

    @Value("${application.default-page-size}")
    private int defaultPageSize;

    @GetMapping("/{restaurantId}")
    @Operation(summary = "Get restaurant by ID",
            description = "Retrieves a restaurant by its ID.")
    @PreAuthorize("hasAnyRole('USER', 'STAFF', 'SELLER', 'MANAGER', 'ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant found"),
            @ApiResponse(responseCode = "400", description = "Invalid restaurant request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Restaurant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> getRestaurantById(@PathVariable Integer restaurantId) {
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(OK.toString())
                                .message("Get restaurant with ID " + restaurantId)
                                .data(restaurantService.getRestaurantDTOById(restaurantId))
                                .build()
                );
    }

    @GetMapping
    @Operation(summary = "Get all restaurants",
            description = "Retrieves all restaurants, with optional pagination and sorting.")
//    @PreAuthorize("hasAnyRole('USER', 'STAFF', 'SELLER', 'MANAGER', 'ADMIN')")
    @PermitAll
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Data retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getAllRestaurants(
            @RequestParam(required = false) Map<String, String> filters,
            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String params,
            @RequestParam(required = false, defaultValue = "id") String sortBy
    ) {
        return ResponseEntity
                .status(OK)
                .body(
                        restaurantService.getAllRestaurantDTOs(PagingRequest.builder()
                                .pageNo(pageNo)
                                .pageSize(pageSize)
                                .params(params)
                                .filters(filters)
                                .sortBy(sortBy)
                                .build())
                );
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Search restaurants by name",
            description = "Search restaurants by name, with optional pagination and sorting.")
    @PreAuthorize("hasAnyRole('USER', 'STAFF', 'SELLER', 'MANAGER', 'ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> getRestaurantsByName(
            @PathVariable String name,
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
                                .message("Search restaurants by name " + name)
                                .data(restaurantService.searchRestaurantsByName(name, pageable))
                                .build()
                );
    }

    @PostMapping
    @Operation(summary = "Create a restaurant",
            description = "Create a restaurant, with optional data.")
    @PreAuthorize("hasAnyRole('SELLER', 'MANAGER', 'ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Restaurant created"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> createRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        restaurantService.createRestaurant(restaurantDTO);
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(CREATED.toString())
                                .message("Create restaurant successfully")
                                .build()
                );
    }

    @PutMapping
    @Operation(summary = "Update a restaurant",
            description = "Update a restaurant, with optional data.")
    @PreAuthorize("hasAnyRole('SELLER', 'MANAGER', 'ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Restaurant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> updateRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        restaurantService.updateRestaurantInfo(restaurantDTO);
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(OK.toString())
                                .message("Update restaurant successfully")
                                .build()
                );
    }

    @PutMapping("/{restaurantId}")
    @Operation(summary = "Switch restaurant availability",
            description = "Switch restaurant availability to open/close")
    @PreAuthorize("hasAnyRole('SELLER', 'MANAGER', 'ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant availability updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Restaurant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> switchAvailability(@PathVariable Integer restaurantId) {
        boolean availability = restaurantService.switchRestaurantAvailability(restaurantId);
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(OK.toString())
                                .message("Switch restaurant's availability to " + availability)
                                .build()
                );
    }

    @DeleteMapping("/{restaurantId}")
    @Operation(summary = "Delete a restaurant",
            description = "Soft delete a restaurant")
    @PreAuthorize("hasAnyRole('SELLER', 'MANAGER', 'ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Restaurant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> deleteRestaurant(@PathVariable Integer restaurantId) {
        restaurantService.deleteRestaurant(restaurantId);
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(OK.toString())
                                .message("Delete restaurant successfully")
                                .build()
                );
    }

    @GetMapping("/{restaurantId}/products")
    @Operation(summary = "Get products by restaurant ID",
            description = "Retrieves products by restaurant ID, with optional pagination and sorting.")
    @PreAuthorize("hasAnyRole('USER', 'STAFF', 'SELLER', 'MANAGER', 'ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "400", description = "Invalid product request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> getProductsByRestaurantId(
            @PathVariable Integer restaurantId,
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
                                .message("Get product with restaurant ID " + restaurantId)
                                .data(productService.getAllProductDTOsByRestaurantId(restaurantId, pageable))
                                .build()
                );
    }

    @Operation(summary = "Get categories by restaurant ID",
            description = "Retrieves categories for a specific restaurant, with optional pagination and sorting.")
    @GetMapping("/{restaurantId}/categories")
    @PreAuthorize("hasRole('USER')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categories found"),
            @ApiResponse(responseCode = "400", description = "Invalid category request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Restaurant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> getAllCategoriesByRestaurantId(
            @PathVariable Integer restaurantId,
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
                                .data(categoryService.getAllCategoriesDTOByRestaurantId(restaurantId, pageable))
                                .build()
                );
    }

}
