package com.foodygo.dto.cart;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItem {
    Integer restaurantId;
    Integer productId;
    String productName;
    Double price;
    Integer quantity;
    List<CartAddOnItem> cartAddOnItems;
}
