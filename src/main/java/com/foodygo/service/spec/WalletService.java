package com.foodygo.service.spec;

import com.foodygo.dto.response.WalletBalanceResponse;

public interface WalletService {

    WalletBalanceResponse getWalletByCustomerId(Integer customerId);
    void processWithdraw(Integer walletId, Double amount);
}
