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
            Double amount,
            String bankCode,
            HttpServletRequest request,
            Integer userId
    ) {
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();

        System.out.println("Amount: " + amount);

        long amountInVND = Math.round(amount * 100);
        vnpParamsMap.put("vnp_Amount", String.valueOf(amountInVND));

        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }

        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        // Thêm userId vào orderInfo
        vnpParamsMap.put("vnp_OrderInfo", String.valueOf(userId));

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
        String responseCode = request.getParameter("vnp_ResponseCode");
        String txnRef = request.getParameter("vnp_TxnRef");
        String amountStr = request.getParameter("vnp_Amount");

        // Lấy thông tin từ orderInfo
        String[] split = request.getParameter("vnp_OrderInfo").split("\\|");
        Integer customerId = Integer.parseInt(request.getParameter("vnp_OrderInfo"));

        if (responseCode.equals("00")) { // Thanh toán thành công
            double amount = Double.parseDouble(amountStr) / 100; // VNPay trả về số tiền nhân 100

            Wallet wallet = walletRepository.findByCustomerId(customerId)
                    .orElseThrow(() -> new IdNotFoundException("Wallet not found for customer"));

            double newBalance = wallet.getBalance() + amount;
            wallet.setBalance(newBalance);
            walletRepository.save(wallet);

            // Lưu thông tin nạp tiền vào bảng Deposit
            Deposit deposit = Deposit.builder()
                    .description("VNPay Deposit")
                    .amount(amount)
                    .time(LocalDateTime.now())
                    .method(DepositMethod.VNPAY)
                    .remaining(newBalance)
                    .customer(wallet.getCustomer())
                    .wallet(wallet)
                    .build();
            depositRepository.save(deposit);

            // Lưu transaction vào bảng Transaction
            Transaction transaction = Transaction.builder()
                    .description("Deposit via VNPay")
                    .time(LocalDateTime.now())
                    .amount(amount)
                    .remaining(newBalance)
                    .type(TransactionType.TOP_UP)
                    .wallet(wallet)
                    .deposit(deposit)
                    .build();
            transactionRepository.save(transaction);

            return ObjectResponse.builder()
                    .status(HttpStatus.OK.toString())
                    .message("Payment Success")
                    .data("Wallet balance updated successfully")
                    .build();
        }

        throw new IdNotFoundException("Payment Failed");
    }

}
