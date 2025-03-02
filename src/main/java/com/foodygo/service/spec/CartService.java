package com.foodygo.service.spec;

import com.foodygo.dto.cart.Cart;
import com.foodygo.dto.cart.CartItem;
import org.springframework.stereotype.Service;

@Service
public interface CartService {
    Cart getCart(Integer userId);

    Cart addToCart(Integer userId, CartItem cartItem);

    Cart removeFromCart(Integer userId, Integer productId);

    Cart clearCart(Integer userId);
}
