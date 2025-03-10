package com.foodygo.service.spec;

import com.foodygo.dto.response.WalletBalanceResponse;
import com.foodygo.entity.Order;
import jakarta.servlet.http.HttpServletRequest;

public interface WalletService {

    WalletBalanceResponse getWalletByCustomerId(Integer customerId);
    void processWithdraw(Integer walletId, Double amount);
    void processTransfer(Integer walletId, String receiver, Integer amount, String note);
    Object processTopUp(Integer walletId, Integer amount, String method, HttpServletRequest request);
    void paymentOrder(Order order);
}
