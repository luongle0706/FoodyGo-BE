package com.foodygo.thirdparty.payos;

import com.foodygo.dto.response.ObjectResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.payos.type.PaymentLinkData;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PayOSController {

    @Value("${payos.client-id}")
    private String clientId;

    @Value("${payos.api-key}")
    private String apiKey;

    @Value("${payos.checksum-key}")
    private String checksumKey;

    @Value("${payos.webhook-url}")
    private String webhookUrl;

    private PayOS payOSClient;

    @Bean
    public PayOS payOSClient() {
        payOSClient = new PayOS(clientId, apiKey, checksumKey);
        return payOSClient;
    }

    @PostMapping("/create-link")
    public ResponseEntity<ObjectResponse> createPaymentLink(@RequestBody CreatePaymentRequest request) {
        try {
            String orderCode = request.getOrderCode();

            if (orderCode == null || orderCode.isEmpty()) {
                return new ResponseEntity<>(
                        ObjectResponse.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .message("Order code is required")
                                .build(),
                        HttpStatus.BAD_REQUEST
                );
            }

            // Use product name from request or default
            String productName = request.getProductName() != null ?
                    request.getProductName() : "Thanh toan dia com";

            // Use amount from request or default
            int amount = request.getAmount() > 0 ?
                    request.getAmount() : 2000;

            // Use description from request or default
            String description = request.getDescription() != null ?
                    request.getDescription() : "Thanh toán đơn hàng";

            ItemData itemData = ItemData.builder()
                    .name(productName)
                    .quantity(1)
                    .price(amount)
                    .build();

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(Long.valueOf(orderCode))
                    .amount(amount)
                    .description(description)
                    .returnUrl(webhookUrl + "/success")
                    .cancelUrl(webhookUrl + "/cancel")
                    .item(itemData)
                    .build();

            CheckoutResponseData result = payOSClient.createPaymentLink(paymentData);

            return new ResponseEntity<>(
                    ObjectResponse.builder()
                            .status(HttpStatus.OK.toString())
                            .message("Success")
                            .data(result)
                            .build(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ObjectResponse.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                            .message("Error: " + e.getMessage())
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/refund")
    public ResponseEntity<ObjectResponse> refundPayment(@RequestBody RefundRequest request) {
        try {
            String orderId = request.getOrderId();
            String reason = request.getReason();

            if (orderId == null || orderId.isEmpty()) {
                return new ResponseEntity<>(
                        ObjectResponse.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .message("Order ID is required")
                                .build(),
                        HttpStatus.BAD_REQUEST
                );
            }

            // Attempt to cancel the payment link
            PaymentLinkData cancelResult = payOSClient.cancelPaymentLink(
                    Long.parseLong(orderId),
                    reason
            );

            // If successful, return the cancellation result
            return new ResponseEntity<>(
                    ObjectResponse.builder()
                            .status(HttpStatus.OK.toString())
                            .message("Payment cancelled successfully")
                            .data(cancelResult)
                            .build(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ObjectResponse.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                            .message("Error processing refund: " + e.getMessage())
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/order-info/{orderCode}")
    public ResponseEntity<ObjectResponse> getPaymentInfo(@PathVariable String orderCode) {
        try {
            PaymentLinkData paymentLinkData = payOSClient.getPaymentLinkInformation(Long.valueOf(orderCode));

            return new ResponseEntity<>(
                    ObjectResponse.builder()
                            .status(HttpStatus.OK.toString())
                            .message("Payment information retrieved successfully")
                            .data(paymentLinkData)
                            .build(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ObjectResponse.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                            .message("Error retrieving payment information: " + e.getMessage())
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/callbackne")
    public void callBack(HttpServletRequest request) {
        System.out.println(request.getParameter("s"));
    }
}