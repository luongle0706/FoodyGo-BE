package com.foodygo.thirdparty.vnpay;

import lombok.Builder;
import lombok.Data;

public abstract class PaymentDTO {
    @Builder
    @Data
    public static class VNPayResponse {
        public String code;
        public String message;
        public String paymentUrl;
    }
}
