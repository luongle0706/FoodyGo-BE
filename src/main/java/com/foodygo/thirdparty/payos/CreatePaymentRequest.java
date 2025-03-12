package com.foodygo.thirdparty.payos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentRequest {
    private String orderCode;
    private String productName;
    private int amount;
    private String description;
    // Add other fields as needed
}