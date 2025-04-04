package com.foodygo.controller;

import com.foodygo.dto.cart.Cart;
import com.foodygo.dto.cart.CartItem;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.service.impl.CartServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")

public class CartController {
    private final CartServiceImpl cartService;

    @GetMapping("/users/{userId}")
    @Operation(summary = "Get Cart By User", description = "Retrieve a cart by the specified user ID.")
    @PreAuthorize("hasAnyRole('USER')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart found"),
            @ApiResponse(responseCode = "400", description = "Invalid cart request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Cart not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> getCart(@PathVariable Integer userId) {
        return ResponseEntity.ok(ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Get cart successfully!")
                        .data(cartService.getCart(userId))
                .build());
    }

    @GetMapping("/users/{userId}/restaurants/{restaurantId}/products/{productId}")
    @Operation(summary = "Get Cart item By restaurant and product", description = "Retrieve a cart item by the specified restaurant ID and product ID.")
    @PreAuthorize("hasAnyRole('USER', 'SELLER')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart item found"),
            @ApiResponse(responseCode = "400", description = "Invalid cart request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Cart item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> getCartItemByProductAndRestaurant(@PathVariable Integer userId,
                                                                            @PathVariable Integer restaurantId,
                                                                            @PathVariable Integer productId) {
        return ResponseEntity.ok(ObjectResponse.builder()
                .status(HttpStatus.OK.toString())
                .message("Get cart item successfully!")
                .data(cartService.getCartItemByProductAndRestaurant(userId, restaurantId, productId))
                .build());
    }

    @GetMapping("/users/{userId}/restaurants/{restaurantId}")
    @Operation(summary = "Get Cart items By restaurant", description = "Retrieve a cart items by the specified restaurant ID.")
    @PreAuthorize("hasAnyRole('USER', 'SELLER')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart item found"),
            @ApiResponse(responseCode = "400", description = "Invalid cart request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Cart item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> getCartItemByProductAndRestaurant(@PathVariable Integer userId, @PathVariable Integer restaurantId) {
        return ResponseEntity.ok(ObjectResponse.builder()
                .status(HttpStatus.OK.toString())
                .message("Get cart item successfully!")
                .data(cartService.getCartItemsByRestaurant(userId, restaurantId))
                .build());
    }

    @PostMapping("/users/{userId}")
    @Operation(summary = "Add To Cart", description = "Add a new product to cart.")
    @PreAuthorize("hasAnyRole('USER')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart found"),
            @ApiResponse(responseCode = "400", description = "Invalid cart request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Cart not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> addToCart(@PathVariable Integer userId, @RequestBody CartItem cartItem) {
        Cart cart = cartService.addToCart(userId, cartItem);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ObjectResponse.builder()
                                .status(HttpStatus.CREATED.toString())
                                .message("Add to cart successfully!")
                                .data(cart)
                                .build()
                );
    }

    @DeleteMapping("/users/{userId}/products/{productId}")
    @Operation(summary = "Remove From Cart", description = "Remove a product from cart.")
    @PreAuthorize("hasAnyRole('USER')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart found"),
            @ApiResponse(responseCode = "400", description = "Invalid cart request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Cart not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> removeFromCart(@PathVariable Integer userId, @PathVariable Integer productId) {
        Cart cart = cartService.removeFromCart(userId, productId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ObjectResponse.builder()
                                .status(HttpStatus.OK.toString())
                                .message("Remove from cart successfully!")
                                .data(cart)
                                .build()
                );
    }

    @DeleteMapping("/users/{userId}")
    @Operation(summary = "Clear Cart", description = "Remove all product from cart.")
    @PreAuthorize("hasAnyRole('USER')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart found"),
            @ApiResponse(responseCode = "400", description = "Invalid cart request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Cart not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> clearCart(@PathVariable Integer userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ObjectResponse.builder()
                                .status(HttpStatus.OK.toString())
                                .message("Clear cart successfully!")
                                .data(cartService.clearCart(userId))
                                .build()
                );
    }

    // In CartController.java, add a new endpoint
    @DeleteMapping("/users/{userId}/products/{productId}/items/{itemIndex}")
    @Operation(summary = "Remove Specific Item From Cart", description = "Remove a specific product item from cart based on index.")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ObjectResponse> removeSpecificItemFromCart(
            @PathVariable Integer userId,
            @PathVariable Integer productId,
            @PathVariable Integer itemIndex) {

        Cart cart = cartService.removeSpecificItemFromCart(userId, productId, itemIndex);
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ObjectResponse.builder()
                                .status(HttpStatus.OK.toString())
                                .message("Remove from cart successfully!")
                                .data(cart)
                                .build()
                );
    }
}
