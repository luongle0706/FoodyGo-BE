package com.foodygo.thirdparty;

import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.entity.Deposit;
import com.foodygo.entity.Transaction;
import com.foodygo.entity.Wallet;
import com.foodygo.enums.DepositMethod;
import com.foodygo.enums.TransactionType;
import com.foodygo.exception.IdNotFoundException;
import com.foodygo.repository.DepositRepository;
import com.foodygo.repository.TransactionRepository;
import com.foodygo.repository.WalletRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final VNPAYConfig vnPayConfig;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final WalletRepository walletRepository;
    private final DepositRepository depositRepository;
    private final TransactionRepository transactionRepository;

    public PaymentDTO.VNPayResponse requestPayment(
            Integer amount,
            String bankCode,
            HttpServletRequest request,
            Integer customerId
    ) {
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();

        System.out.println("Amount: " + amount);

        long amountInVND = amount * 100;
        vnpParamsMap.put("vnp_Amount", String.valueOf(amountInVND));

        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }

        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        // Thêm userId vào orderInfo
        vnpParamsMap.put("vnp_OrderInfo", String.valueOf(customerId));

        return getVnPayResponse(vnpParamsMap);
    }

    private PaymentDTO.VNPayResponse getVnPayResponse(Map<String, String> vnpParamsMap) {
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return PaymentDTO.VNPayResponse.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl)
                .build();
    }

    public PaymentDTO.VNPayResponse createVnPayPayment(HttpServletRequest request) {
        long amount = Integer.parseInt(request.getParameter("amount")) * 100L;
        String bankCode = request.getParameter("bankCode");
        String userId = request.getParameter("userId");
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
        // Thêm orderId vào orderInfo
        vnpParamsMap.put("vnp_OrderInfo", String.valueOf(userId));
        //build query url
        return getVnPayResponse(vnpParamsMap);
    }

    public ObjectResponse handleVNPayCallback(HttpServletRequest request) {
        // Extract parameters from the callback request
        String responseCode = request.getParameter("vnp_ResponseCode");
        String txnRef = request.getParameter("vnp_TxnRef");
        String amountStr = request.getParameter("vnp_Amount");
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String bankCode = request.getParameter("vnp_BankCode");
        String transactionNo = request.getParameter("vnp_TransactionNo");
        String payDate = request.getParameter("vnp_PayDate");

        // Parse customer ID from the order info
        Integer customerId;
        try {
            // If orderInfo contains additional data separated by |
            if (orderInfo.contains("|")) {
                String[] orderInfoParts = orderInfo.split("\\|");
                customerId = Integer.parseInt(orderInfoParts[0]);
            } else {
                // If orderInfo is simply the customer ID
                customerId = Integer.parseInt(orderInfo);
            }
        } catch (NumberFormatException e) {
            throw new IdNotFoundException("Invalid customer ID in order info: " + orderInfo);
        }

        // Check if payment was successful
        if (responseCode.equals("00")) {
            // Convert amount from VNPay format (amount * 100) to actual amount
            double amountInVND = Double.parseDouble(amountStr) / 100;

            // Convert VND amount to FoodyXu (1000 VND = 1 FoodyXu)
            double foodyXuAmount = amountInVND / 1000;

            // Find customer's wallet
            Wallet wallet = walletRepository.findByCustomerId(customerId)
                    .orElseThrow(() -> new IdNotFoundException("Wallet not found for customer ID: " + customerId));

            // Create transaction description with detailed information
            String depositDescription = String.format(
                    "Nạp tiền qua VNPAY - Mã GD: %s - Ngân hàng: %s",
                    transactionNo,
                    bankCode
            );

            // Calculate new balance
            double newBalance = wallet.getBalance() + foodyXuAmount;

            // Create Deposit record
            Deposit deposit = Deposit.builder()
                    .description(depositDescription)
                    .amount(foodyXuAmount)
                    .time(LocalDateTime.now())
                    .method(DepositMethod.VNPAY)
                    .remaining(newBalance)
                    .customer(wallet.getCustomer())
                    .wallet(wallet)
                    .build();

            // Save deposit record first to get its ID
            depositRepository.save(deposit);

            // Create Transaction record linked to the deposit
            Transaction transaction = Transaction.builder()
                    .description("Nạp " + foodyXuAmount + " FoodyXu qua VNPAY")
                    .time(LocalDateTime.now())
                    .amount(foodyXuAmount)
                    .remaining(newBalance)
                    .type(TransactionType.TOP_UP)
                    .wallet(wallet)
                    .deposit(deposit)  // Link to the deposit record
                    .build();

            // Save transaction record
            transactionRepository.save(transaction);

            // Update wallet balance
            wallet.setBalance(newBalance);
            walletRepository.save(wallet);

            // Return success response
            return ObjectResponse.builder()
                    .status(HttpStatus.OK.toString())
                    .message("Payment Success")
                    .data(Map.of(
                            "transactionId", transaction.getId(),
                            "depositId", deposit.getId(),
                            "amount", foodyXuAmount,
                            "newBalance", newBalance
                    ))
                    .build();
        } else {
            // If payment failed, log the error code and return failure response
            String errorMessage = "Payment failed with response code: " + responseCode;

            // You could add error logging here

            throw new IdNotFoundException(errorMessage);
        }
    }

}
