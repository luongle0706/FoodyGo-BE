package com.foodygo.controller;

import com.foodygo.dto.CategoryDTO;
import com.foodygo.dto.internal.PagingRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.service.spec.CategoryService;
import com.foodygo.service.spec.ProductService;
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

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Category")
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductService productService;

    @Value("${application.default-page-size}")
    private int defaultPageSize;

    @GetMapping
    @Operation(summary = "Get all categories",
            description = "Retrieves all categories, with optional pagination and sorting.")
    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN','SELLER','MANAGER')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Data retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<MappingJacksonValue> getAllCategories(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String params,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false) Map<String, String> filters
    ) {
        filters.remove("pageNo");
        filters.remove("pageSize");
        filters.remove("sortBy");
        filters.remove("params");
        return ResponseEntity
                .status(OK)
                .body(
                        categoryService.getAllCategories(PagingRequest.builder()
                                .pageNo(pageNo)
                                .pageSize(pageSize)
                                .params(params)
                                .sortBy(sortBy)
                                .filters(filters)
                                .build())
                );
    }

    @Operation(summary = "Get category by ID",
            description = "Retrieves a category by its ID.")
    @GetMapping("/{categoryId}")
    @PreAuthorize("hasAnyRole('USER','STAFF','ADMIN','SELLER','MANAGER')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "400", description = "Invalid category request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> getCategoryById(
            @PathVariable Integer categoryId
    ) {
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(OK.toString())
                                .message("Get category by id " + categoryId)
                                .data(categoryService.getCategoryDTOById(categoryId))
                                .build()
                );
    }

    @Operation(summary = "Create a new category",
            description = "Creates a new category with the provided details.")
    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN','SELLER')")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created"),
            @ApiResponse(responseCode = "400", description = "Invalid category body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> createCategory(
            @RequestBody CategoryDTO.CategoryCreateRequest request
    ) {
        return ResponseEntity
                .status(CREATED)
                .body(
                        ObjectResponse.builder()
                                .status(CREATED.toString())
                                .message("Created category")
                                .data(categoryService.createCategoryDTO(request))
                                .build()
                );
    }

    @Operation(summary = "Update an existing category",
            description = "Updates the details of an existing category.")
    @PutMapping
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN','SELLER')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated"),
            @ApiResponse(responseCode = "400", description = "Invalid category body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> updateCategory(
            @RequestBody CategoryDTO.CategoryUpdateRequest request
    ) {
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(OK.toString())
                                .message("Updated category")
                                .data(categoryService.updateCategoryDTO(request))
                                .build()
                );
    }

    @Operation(summary = "Delete a category",
            description = "Deletes a category by its ID.")
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN','SELLER')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category availability updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> deleteCategory(
            @PathVariable Integer categoryId
    ) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(CREATED.toString())
                                .message("Category deleted")
                                .build()
                );
    }

    @GetMapping("/{categoryId}/products")
    @Operation(summary = "Get products by category ID",
            description = "Retrieves products by category ID, with optional pagination and sorting.")
    @PreAuthorize("hasAnyRole('USER', 'STAFF', 'SELLER', 'MANAGER', 'ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "400", description = "Invalid product request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> getProductsByCategoryId(
            @PathVariable Integer categoryId,
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
                                .message("Get product with category ID " + categoryId)
                                .data(productService.getAllProductDTOsByCategoryId(categoryId, pageable))
                                .build()
                );
    }
}
