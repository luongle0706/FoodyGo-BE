package com.foodygo.controller;

import com.foodygo.dto.internal.PagingRequest;
import com.foodygo.dto.product.ProductCreateRequest;
import com.foodygo.dto.product.ProductUpdateRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.service.spec.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Tag(name = "Product")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get all products",
            description = "Retrieves all products, with optional pagination and sorting.")
    @PreAuthorize("hasAnyRole('USER', 'STAFF', 'SELLER', 'MANAGER', 'ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Data retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<MappingJacksonValue> getAllProducts(
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
                .body(productService.getProducts(
                        PagingRequest.builder()
                                .pageNo(pageNo)
                                .pageSize(pageSize)
                                .sortBy(sortBy)
                                .filters(filters)
                                .params(params)
                                .build()));
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get product by ID",
            description = "Retrieves a product by its ID.")
    @PreAuthorize("hasAnyRole('USER', 'STAFF', 'SELLER', 'MANAGER', 'ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "400", description = "Invalid product request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> getProductById(@PathVariable Integer productId) {
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(OK.toString())
                                .message("Get product with ID " + productId)
                                .data(productService.getProductDTOById(productId))
                                .build()
                );
    }

    @Operation(summary = "Create a new product",
            description = "Creates a new product with the provided details.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    @PreAuthorize("hasAnyRole('SELLER', 'MANAGER', 'ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created"),
            @ApiResponse(responseCode = "400", description = "Invalid product body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> createProduct(
            @RequestPart("data") ProductCreateRequest request,
            @RequestPart("image") MultipartFile image
    ) {
        productService.createProduct(request, image);
        return ResponseEntity
                .status(CREATED)
                .body(
                        ObjectResponse.builder()
                                .status(CREATED.toString())
                                .message("Creat product successfully")
                                .build()
                );
    }

    @Operation(summary = "Update an existing product",
            description = "Updates the details of an existing product.")
    @PutMapping("/{productId}")
    @PreAuthorize("hasAnyRole('SELLER', 'MANAGER', 'ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated"),
            @ApiResponse(responseCode = "400", description = "Invalid product body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> updateProduct(
            @PathVariable Integer productId,
            @RequestPart("data") ProductUpdateRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        productService.updateProductInfo(image, productId, request);
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(OK.toString())
                                .message("Update product successfully")
                                .build()
                );
    }

    @PutMapping("/{productId}/availability")
    @Operation(summary = "Switch product availability",
            description = "Switch product availability to open/close")
    @PreAuthorize("hasAnyRole('SELLER', 'MANAGER', 'ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product availability updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> switchAvailability(@PathVariable Integer productId) {
        boolean availability = productService.switchProductAvailability(productId);
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(OK.toString())
                                .message("Switch product's availability to " + availability)
                                .build()
                );
    }

    @Operation(summary = "Delete a product",
            description = "Deletes a product by its ID.")
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyRole('SELLER', 'MANAGER', 'ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> deleteProduct(
            @PathVariable Integer productId
    ) {
        productService.deleteProduct(productId);
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(CREATED.toString())
                                .message("Delete product successfully")
                                .build()
                );
    }

    @Operation(summary = "Link an addon-section to product",
            description = "Deletes a product by its ID.")
    @PutMapping("/{productId}/addon-sections/{addonSectionId}")
    @PreAuthorize("hasAnyRole('SELLER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<ObjectResponse> link(
            @PathVariable Integer addonSectionId,
            @PathVariable Integer productId
    ) {
        productService.linkAddonSection(addonSectionId, productId);
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(OK.toString())
                                .message("Product update successfully")
                                .build()
                );
    }
}

