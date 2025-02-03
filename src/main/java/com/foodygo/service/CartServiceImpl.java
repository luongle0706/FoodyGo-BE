package com.foodygo.service;

import com.foodygo.dto.cart.Cart;
import com.foodygo.dto.cart.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CART_PREFIX = "cart:";

    public Cart getCart(Integer userId) {

        String key = CART_PREFIX + userId;
        Cart cart = (Cart) redisTemplate.opsForValue().get(key);
        return cart != null ? cart : new Cart();
    }

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

    public Cart removeFromCart(Integer userId, Integer productId) {
        Cart cart = getCart(userId);
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        if (cart.getItems().isEmpty()) {
            redisTemplate.delete(CART_PREFIX + userId);
            return new Cart();
        }
        return updateCart(userId, cart);
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

    public Cart clearCart(Integer userId) {
        redisTemplate.delete(CART_PREFIX + userId);
        return new Cart();
    }

}
