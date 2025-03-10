package com.foodygo.controller;

import com.foodygo.dto.request.OrderCreateRequest;
import com.foodygo.dto.request.OrderUpdateRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.dto.response.OrderResponse;
import com.foodygo.enums.OrderStatus;
import com.foodygo.service.spec.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PreAuthorize("hasAnyRole('USER')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "400", description = "Invalid Order request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> createOrder(
            @RequestBody OrderCreateRequest request
    ) {
        System.out.println("Order create request");
        System.out.println(request);
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
    @PreAuthorize("hasAnyRole('STAFF', 'SELLER', 'MANAGER', 'ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "400", description = "Invalid Order request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
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
    @Operation(summary = "Get Order By Id", description = "Retrieve a paginated list of all orders. Supports sorting and pagination.")
    @PreAuthorize("hasAnyRole('USER', 'STAFF', 'SELLER', 'MANAGER', 'ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "400", description = "Invalid Order request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
    @PreAuthorize("hasAnyRole('USER', 'STAFF', 'SELLER', 'MANAGER', 'ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "400", description = "Invalid Order request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> getAllOrders(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending,
            @RequestParam(required = false) OrderStatus status
    ) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize != null ? pageSize : defaultPageSize, sort);
        Page<OrderResponse> orders;
        if (status != null) {
            orders = orderService.getOrdersByStatus(status, pageable);
        } else {
            orders = orderService.getAllOrders(pageable);
        }
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(OK.toString())
                                .message("Get all orders successfully!")
                                .data(orders)
                                .build()
                );
    }

    @GetMapping("/employees/{employeeId}")
    @Operation(summary = "Get Orders By Employee", description = "Retrieve a paginated list of all orders by the specified employee ID. Supports sorting and pagination.")
    @PreAuthorize("hasAnyRole('STAFF', 'MANAGER', 'ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart found"),
            @ApiResponse(responseCode = "400", description = "Invalid Order request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "400", description = "Invalid Order request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
    @PreAuthorize("hasAnyRole('SELLER', 'MANAGER', 'ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "400", description = "Invalid Order request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
