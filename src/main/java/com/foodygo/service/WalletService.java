package com.foodygo.service;

import com.foodygo.dto.response.WalletBalanceResponse;
import com.foodygo.entity.Wallet;

public interface WalletService {

    WalletBalanceResponse getWalletByCustomerId(Integer customerId);
    WalletBalanceResponse getWalletByRestaurantId(Integer restaurantId);
    double getWalletBalance(Integer walletId);
    Wallet createWallet(Wallet wallet);
    void deleteWallet(Integer walletId);

}
