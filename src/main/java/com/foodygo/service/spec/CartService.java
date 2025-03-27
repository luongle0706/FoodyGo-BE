package com.foodygo.service.spec;

import com.foodygo.dto.cart.Cart;
import com.foodygo.dto.cart.CartItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
    Cart getCart(Integer userId);

    Cart addToCart(Integer userId, CartItem cartItem);

    Cart removeFromCart(Integer userId, Integer productId);

    Cart clearCart(Integer userId);

    Cart removeSpecificItemFromCart(Integer userId, Integer productId, Integer itemIndex);

    CartItem getCartItemByProductAndRestaurant(Integer userId, Integer restaurantId, Integer productId);
    List<CartItem> getCartItemsByRestaurant(Integer userId, Integer restaurantId);
}
