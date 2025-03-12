package com.foodygo.thirdparty.payos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefundRequest {
    private String orderId;
    private int amount;
    private String reason;
}