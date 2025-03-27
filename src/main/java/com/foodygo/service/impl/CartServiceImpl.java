package com.foodygo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodygo.dto.cart.Cart;
import com.foodygo.dto.cart.CartAddOnItem;
import com.foodygo.dto.cart.CartItem;
import com.foodygo.exception.IdNotFoundException;
import com.foodygo.service.spec.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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

        // Create a unique key for each product+addon combination
        boolean uniqueItemFound = false;

        // Only merge identical items (same product + same addons)
        for (CartItem existingItem : cart.getItems()) {
            if (existingItem.getProductId().equals(cartItem.getProductId()) &&
                    hasSameAddons(existingItem.getCartAddOnItems(), cartItem.getCartAddOnItems())) {
                existingItem.setQuantity(existingItem.getQuantity() + cartItem.getQuantity());
                uniqueItemFound = true;
                break;
            }
        }

        // If no matching item found, add as new item
        if (!uniqueItemFound) {
            cart.getItems().add(cartItem);
        }

        return updateCart(userId, cart);
    }

    // Add this helper method to CartServiceImpl
    private boolean hasSameAddons(List<CartAddOnItem> list1, List<CartAddOnItem> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }

        // Sort both lists by addOnItemId for consistent comparison
        list1.sort(Comparator.comparing(CartAddOnItem::getAddOnItemId));
        list2.sort(Comparator.comparing(CartAddOnItem::getAddOnItemId));

        for (int i = 0; i < list1.size(); i++) {
            CartAddOnItem item1 = list1.get(i);
            CartAddOnItem item2 = list2.get(i);

            if (!item1.getAddOnItemId().equals(item2.getAddOnItemId()) ||
                    !item1.getQuantity().equals(item2.getQuantity())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Cart removeFromCart(Integer userId, Integer productId) {
        Cart cart = getCart(userId);

        // Find the first occurrence of the product (this will need enhancement)
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

    // In CartServiceImpl.java, ensure the updateCart method calculates prices correctly
    public Cart updateCart(Integer userId, Cart cart) {
        double totalPrice = 0.0;

        for (CartItem item : cart.getItems()) {
            // Base price of the product
            double itemTotal = item.getPrice() * item.getQuantity();

            // Add addon prices
            if (item.getCartAddOnItems() != null) {
                for (CartAddOnItem addon : item.getCartAddOnItems()) {
                    itemTotal += addon.getPrice() * addon.getQuantity();
                }
            }

            totalPrice += itemTotal;
        }

        cart.setTotalPrice(totalPrice);
        redisTemplate.opsForValue().set(CART_PREFIX + userId, cart);
        return cart;
    }

    @Override
    public Cart removeSpecificItemFromCart(Integer userId, Integer productId, Integer itemIndex) {
        Cart cart = getCart(userId);

        // Find all items matching the productId
        List<CartItem> matchingItems = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .toList();

        // Remove the specific item if index is valid
        if (itemIndex >= 0 && itemIndex < matchingItems.size()) {
            CartItem itemToRemove = matchingItems.get(itemIndex);

            if (itemToRemove.getQuantity() > 1) {
                itemToRemove.setQuantity(itemToRemove.getQuantity() - 1);
            } else {
                cart.getItems().remove(itemToRemove);
            }
        }

        if (cart.getItems().isEmpty()) {
            redisTemplate.delete(CART_PREFIX + userId);
            return new Cart();
        }
        return updateCart(userId, cart);
    }

}
