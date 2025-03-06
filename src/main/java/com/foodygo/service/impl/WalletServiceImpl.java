package com.foodygo.service.impl;

import com.foodygo.dto.response.WalletBalanceResponse;
import com.foodygo.entity.Transaction;
import com.foodygo.entity.Wallet;
import com.foodygo.enums.TransactionType;
import com.foodygo.exception.IdNotFoundException;
import com.foodygo.mapper.WalletMapper;
import com.foodygo.repository.TransactionRepository;
import com.foodygo.repository.WalletRepository;
import com.foodygo.service.spec.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Override
public WalletBalanceResponse getWalletByCustomerId(Integer customerId) {
    Wallet wallet = walletRepository.findByCustomerId(customerId)
            .orElseThrow(() -> new IdNotFoundException("Wallet not found for customer: " + customerId));
    return WalletBalanceResponse.builder()
            .id(wallet.getId())
            .fullName(wallet.getCustomer().getUser().getFullName())
            .balance(wallet.getBalance())
            .build();
    }

    @Override
    public void processWithdraw(Integer walletId, Double amount) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new IdNotFoundException("Cannot found the wallet with id: " + walletId));
        if (amount <= 0) {
            throw new IdNotFoundException("Number of foodyxu must be more than 0");
        }
        if (wallet.getBalance() <= amount) {
            throw new IdNotFoundException("The balance is not enough to withdraw");
        }
        Transaction transaction = Transaction.builder()
                .description("Rút tiền từ ví")
                .amount(amount )
                .remaining(wallet.getBalance() - amount)
                .type(TransactionType.WITHDRAWAL)
                .wallet(wallet)
                .build();
        wallet.setBalance(wallet.getBalance() - amount );
        transactionRepository.save(transaction);
        walletRepository.save(wallet);
    }
}