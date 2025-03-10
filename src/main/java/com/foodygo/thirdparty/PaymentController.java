package com.foodygo.thirdparty;

import com.foodygo.dto.response.ObjectResponse;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/vn-pay")
    @PermitAll
    public ResponseEntity<ObjectResponse> pay(HttpServletRequest request) {
        ObjectResponse response = ObjectResponse.builder()
                .status(HttpStatus.OK.toString())
                .message("Success")
                .data(paymentService.createVnPayPayment(request))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/vn-pay-callback")
    @PermitAll
    public void payCallbackHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectResponse paymentResponse = paymentService.handleVNPayCallback(request);
        response.sendRedirect("https://admin.foodygo.theanh0804.id.vn/payment-successfully");
    }
}

