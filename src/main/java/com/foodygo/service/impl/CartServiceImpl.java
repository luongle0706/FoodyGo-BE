package com.foodygo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodygo.dto.cart.Cart;
import com.foodygo.dto.cart.CartItem;
import com.foodygo.exception.IdNotFoundException;
import com.foodygo.service.spec.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String CART_PREFIX = "cart:";

    @Override
    public Cart getCart(Integer userId) {
        String key = CART_PREFIX + userId;
        Object data = redisTemplate.opsForValue().get(key);
        if (data != null) {
            return objectMapper.convertValue(data, Cart.class);
        }
        return new Cart();
    }

    @Override
    public Cart addToCart(Integer userId, CartItem cartItem) {
        Cart cart = getCart(userId);

        Optional<CartItem> existingItem = cart.getItems()
                .stream()
                .filter(item -> item.getProductId().equals(cartItem.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + cartItem.getQuantity());
        } else {
            cart.getItems().add(cartItem);
        }
        return updateCart(userId, cart);
    }

    @Override
    public Cart removeFromCart(Integer userId, Integer productId) {
        Cart cart = getCart(userId);
        Optional<CartItem> existingItem = cart.getItems()
                .stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
            } else {
                cart.getItems().remove(item);
            }
        }

        if (cart.getItems().isEmpty()) {
            redisTemplate.delete(CART_PREFIX + userId);
            return new Cart();
        }
        return updateCart(userId, cart);
    }


    @Override
    public Cart clearCart(Integer userId) {
        redisTemplate.delete(CART_PREFIX + userId);
        return new Cart();
    }

    @Override
    public CartItem getCartItemByProductAndRestaurant(Integer userId, Integer restaurantId, Integer productId) {
        Cart cart = getCart(userId);
        Optional<CartItem> existingItem = cart.getItems()
                .stream()
                .filter(item -> item.getRestaurantId().equals(restaurantId) && item.getProductId().equals(productId))
                .findFirst();
        if (existingItem.isPresent()) {
            return existingItem.get();
        } else {
            throw new IdNotFoundException("Cart item not found!");
        }
    }

    @Override
    public List<CartItem> getCartItemsByRestaurant(Integer userId, Integer restaurantId) {
       Cart cart = getCart(userId);
        return cart.getItems()
                .stream()
                .filter(item -> item.getRestaurantId().equals(restaurantId)).toList();
    }

    public Cart updateCart(Integer userId, Cart cart) {
        cart.setTotalPrice(cart.getItems()
                .stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity()
                        + item.getCartAddOnItems().stream().mapToDouble(a -> a.getPrice() * a.getQuantity()).sum())
                .sum()
        );

        redisTemplate.opsForValue().set(CART_PREFIX + userId, cart);
        return cart;
    }
}
