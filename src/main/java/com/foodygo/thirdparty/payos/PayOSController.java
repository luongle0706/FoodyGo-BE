package com.foodygo.thirdparty.payos;

import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.entity.Deposit;
import com.foodygo.entity.Transaction;
import com.foodygo.entity.User;
import com.foodygo.entity.Wallet;
import com.foodygo.enums.DepositMethod;
import com.foodygo.enums.DepositStatus;
import com.foodygo.enums.TransactionType;
import com.foodygo.exception.IdNotFoundException;
import com.foodygo.repository.DepositRepository;
import com.foodygo.repository.TransactionRepository;
import com.foodygo.repository.UserRepository;
import com.foodygo.repository.WalletRepository;
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

import javax.annotation.security.PermitAll;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PayOSController {

    private final UserRepository userRepository;
    @Value("${payos.client-id}")
    private String clientId;

    @Value("${payos.api-key}")
    private String apiKey;

    @Value("${payos.checksum-key}")
    private String checksumKey;

    @Value("${payos.webhook-url}")
    private String webhookUrl;

    private PayOS payOSClient;

    private final WalletRepository walletRepository;
    private final DepositRepository depositRepository;
    private final TransactionRepository transactionRepository;

    @Bean
    public PayOS payOSClient() {
        payOSClient = new PayOS(clientId, apiKey, checksumKey);
        return payOSClient;
    }

    @PostMapping("/create-link")
    public ResponseEntity<ObjectResponse> createPaymentLink(@RequestBody CreatePaymentRequest request) {
        try {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new IdNotFoundException("Không tìm thấy người dùng có id là: " + request.getUserId()));

            Wallet wallet = user.getCustomer().getWallet();

            Deposit deposit = Deposit.builder()
                    .description("Nạp tiền vào ví foodygo")
                    .amount(Double.valueOf(request.getAmount()))
                    .time(LocalDateTime.now())
                    .method(DepositMethod.PAYOS)
                    .remaining((wallet.getBalance() + request.getAmount()))
                    .customer(user.getCustomer())
                    .status(DepositStatus.PENDING)
                    .wallet(wallet)
                    .build();

            Deposit saved= depositRepository.save(deposit);

            String productName ="FoodyXu";
            int amount = request.getAmount() > 0 ?
                    request.getAmount() : 1000;
            String description = "Nạp tiền vào ví foodygo: " + saved.getId();

            ItemData itemData = ItemData.builder()
                    .name(productName)
                    .quantity(1)
                    .price(amount)
                    .build();

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(Long.valueOf(saved.getId()))
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

    @GetMapping("/payos-callback/success")
    public ResponseEntity<ObjectResponse> callBack(HttpServletRequest request) {
        try {
            String responseCode = request.getParameter("code");
            String orderCode = request.getParameter("orderCode");
            String paymentId = request.getParameter("id");
            String status = request.getParameter("status");

            if (responseCode.equals("00") && "PAID".equals(status)) {
                PaymentLinkData paymentData = payOSClient.getPaymentLinkInformation(Long.valueOf(orderCode));

                if (paymentData == null) {
                    return new ResponseEntity<>(
                            ObjectResponse.builder()
                                    .status(HttpStatus.BAD_REQUEST.toString())
                                    .message("Unable to verify payment information")
                                    .build(),
                            HttpStatus.BAD_REQUEST
                    );
                } else {
                    paymentData.getAmount();
                }

                double amountInVND = paymentData.getAmount();

                double foodyXuAmount = amountInVND / 1000;

                Deposit deposit = depositRepository.findById(Integer.valueOf(orderCode)).orElseThrow(() -> new IdNotFoundException("Không tìm thấy lần nạp tieenff có mã số: " + orderCode));

                Wallet wallet = deposit.getWallet();

                String depositDescription = String.format(
                        "Nạp tiền qua PayOS - Mã GD: %s",
                        paymentId
                );

                double newBalance = wallet.getBalance() + foodyXuAmount;

                deposit.setAmount(foodyXuAmount);
                deposit.setStatus(DepositStatus.PAID);
                deposit.setDescription(depositDescription);
                depositRepository.save(deposit);

                Transaction transaction = Transaction.builder()
                        .description("Nạp " + foodyXuAmount + " FoodyXu qua PayOS")
                        .time(LocalDateTime.now())
                        .amount(foodyXuAmount)
                        .remaining(newBalance)
                        .type(TransactionType.TOP_UP)
                        .wallet(wallet)
                        .deposit(deposit)
                        .build();

                transactionRepository.save(transaction);

                wallet.setBalance(newBalance);
                walletRepository.save(wallet);

                return new ResponseEntity<>(
                        ObjectResponse.builder()
                                .status(HttpStatus.OK.toString())
                                .message("Payment Success")
                                .data(Map.of(
                                        "transactionId", transaction.getId(),
                                        "depositId", deposit.getId(),
                                        "amount", foodyXuAmount,
                                        "newBalance", newBalance
                                ))
                                .build(),
                        HttpStatus.OK
                );
            } else {
                String errorMessage = "Payment failed with response code: " + responseCode + ", status: " + status;
                return new ResponseEntity<>(
                        ObjectResponse.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .message(errorMessage)
                                .build(),
                        HttpStatus.BAD_REQUEST
                );
            }
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ObjectResponse.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                            .message("Error processing payment callback: " + e.getMessage())
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/payos-callback/cancel")
    public void handleCancel(HttpServletRequest request) {
        String orderCode = request.getParameter("orderCode");
        depositRepository.deleteById(Integer.valueOf(orderCode));
    }
}