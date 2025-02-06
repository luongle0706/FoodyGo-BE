package com.foodygo.service;

import com.foodygo.dto.response.WalletBalanceResponse;
import com.foodygo.entity.Wallet;
import com.foodygo.exception.IdNotFoundException;
import com.foodygo.mapper.WalletMapper;
import com.foodygo.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService{

    private final WalletRepository walletRepository;

    @Override
    public WalletBalanceResponse getWalletByCustomerId(Integer customerId) {
        return WalletMapper.INSTANCE.toDTO( walletRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new IdNotFoundException("Wallet not found for customer: " + customerId)));
    }

    @Override
    public WalletBalanceResponse getWalletByRestaurantId(Integer restaurantId) {
        return WalletMapper.INSTANCE.toDTO( walletRepository.findByRestaurantId(restaurantId)
                .orElseThrow(() -> new IdNotFoundException("Wallet not found for restaurant: " + restaurantId)));
    }

    @Override
    public double getWalletBalance(Integer walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new IdNotFoundException("Wallet not found"));
        return wallet.getBalance();
    }

    @Override
    public Wallet createWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Override
    public void deleteWallet(Integer walletId) {
        if (!walletRepository.existsById(walletId)) {
            throw new IdNotFoundException("Wallet not found");
        }
        walletRepository.deleteById(walletId);
    }
}