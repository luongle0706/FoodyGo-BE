package com.foodygo.controller;

import com.foodygo.dto.cart.Cart;
import com.foodygo.dto.cart.CartItem;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.service.CartServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")

public class CartController {
    private final CartServiceImpl cartService;

    @GetMapping("/users/{userId}")
    @Operation(summary = "Get Cart By User", description = "Retrieve a cart by the specified restaurant ID.")
    public ResponseEntity<ObjectResponse> getCart(@PathVariable Integer userId) {
        return ResponseEntity.ok(ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Get cart successfully!")
                        .data(cartService.getCart(userId))
                .build());
    }

    @PostMapping("/users/{userId}")
    @Operation(summary = "Add To Cart", description = "Add a new product to cart.")
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
}
