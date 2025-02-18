package com.foodygo.controller;

import com.foodygo.dto.request.OrderCreateRequest;
import com.foodygo.dto.request.OrderUpdateRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Tag(name = "Order")
public class OrderController {

    private final OrderService orderService;

    @Value("${application.default-page-size}")
    private int defaultPageSize;

    @PostMapping()
    @Operation(summary = "Create Order", description = "Create a new order using the provided data.")
    public ResponseEntity<ObjectResponse> createOrder(
            @RequestBody OrderCreateRequest request
    ) {
        return ResponseEntity
                .status(CREATED)
                .body(
                        ObjectResponse.builder()
                                .status(CREATED.toString())
                                .message("Create order successfully!")
                                .data(orderService.createOrder(request))
                                .build()
                );
    }

    @PutMapping("/{orderId}")
    @Operation(summary = "Update Order", description = "Update an existing order with the provided data.")
    public ResponseEntity<ObjectResponse> updateAddonItem(
            @PathVariable Integer orderId,
            @RequestBody OrderUpdateRequest orderUpdateRequest
    ) {
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(OK.toString())
                                .message("Update order successfully!")
                                .data(orderService.updateOrder(orderId, orderUpdateRequest))
                                .build()
                );
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "Delete Order", description = "Delete an order by its unique ID.")
    public ResponseEntity<ObjectResponse> deleteOrder(
            @PathVariable Integer orderId
    ) {
        orderService.deleteOrder(orderId);
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(OK.toString())
                                .status("Delete order")
                                .message("Delete successfully!")
                                .build()
                );
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get All Orders", description = "Retrieve a paginated list of all orders. Supports sorting and pagination.")
    public ResponseEntity<ObjectResponse> getOrderById(@PathVariable Integer orderId) {
        return ResponseEntity.ok(
                ObjectResponse.builder()
                        .status(OK.toString())
                        .message("Get order by Id")
                        .data(orderService.getOrderResponseById(orderId))
                        .build()
        );
    }

    @GetMapping()
    @Operation(summary = "Get All Orders", description = "Retrieve a paginated list of all orders. Supports sorting and pagination.")
    public ResponseEntity<ObjectResponse> getAllOrders(
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
                                .message("Get all orders successfully!")
                                .data(orderService.getAllOrders(pageable))
                                .build()
                );
    }

    @GetMapping("/employees/{employeeId}")
    @Operation(summary = "Get Orders By Employee", description = "Retrieve a paginated list of all orders by the specified employee ID. Supports sorting and pagination.")
    public ResponseEntity<ObjectResponse> getAllOrdersByEmployeeId(
            @PathVariable Integer employeeId,
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
                                .message("Get all orders by employee ID successfully!")
                                .data(orderService.getAllOrdersByEmployeeId(employeeId, pageable))
                                .build()
                );
    }

    @GetMapping("/customers/{customerId}")
    @Operation(summary = "Get Orders By Customer", description = "Retrieve a paginated list of all orders by the specified customer ID. Supports sorting and pagination.")
    public ResponseEntity<ObjectResponse> getAllOrdersByCustomerId(
            @PathVariable Integer customerId,
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
                                .message("Get all orders by customer ID successfully!")
                                .data(orderService.getAllOrdersByCustomerId(customerId, pageable))
                                .build()
                );
    }

    @GetMapping("/restaurants/{restaurantId}")
    @Operation(summary = "Get Orders By Restaurant", description = "Retrieve a paginated list of all orders by the specified restaurant ID. Supports sorting and pagination.")
    public ResponseEntity<ObjectResponse> getAllOrdersByRestaurantId(
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
                                .message("Get all orders by restaurant ID successfully!")
                                .data(orderService.getAllOrdersByCustomerId(restaurantId, pageable))
                                .build()
                );
    }

}
