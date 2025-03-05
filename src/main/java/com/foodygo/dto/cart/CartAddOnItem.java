package com.foodygo.dto.cart;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartAddOnItem {
    Integer addOnItemId;
    String addOnItemName;
    Double price;
    Integer quantity;
}
