package com.foodygo.service.impl;
import com.foodygo.dto.response.TransactionHistoryResponse;
import com.foodygo.entity.Transaction;
import com.foodygo.entity.Wallet;
import com.foodygo.enums.TransactionType;
import com.foodygo.exception.IdNotFoundException;
import com.foodygo.mapper.TransactionMapper;
import com.foodygo.repository.TransactionRepository;
import com.foodygo.repository.WalletRepository;
import com.foodygo.service.spec.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    @Override
    public List<TransactionHistoryResponse> getTransactionsByWallet(Integer walletId) {
        return TransactionMapper.INSTANCE.toDTO(transactionRepository.findByWalletId(walletId));
    }

    @Override
    public TransactionHistoryResponse getTransactionById(Integer transactionId) {
        return TransactionMapper.INSTANCE.toDTO(transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IdNotFoundException("Transaction not found")));
    }

    @Override
    public List<TransactionHistoryResponse> getTransactionsByType(Integer walletId, TransactionType type) {
        return TransactionMapper.INSTANCE.toDTO(transactionRepository.findByWalletIdAndType(walletId, type));
    }

    @Override
    public TransactionHistoryResponse processPayment(Integer walletId, double amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new IdNotFoundException("Wallet not found"));

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient balance");
        }

        wallet.setBalance(wallet.getBalance() - (amount));
        walletRepository.save(wallet);
        Transaction transaction = Transaction.builder()
                .amount(amount)
                .remaining(wallet.getBalance())
                .type(TransactionType.PAYMENT)
                .wallet(wallet)
                .build();
        return TransactionMapper.INSTANCE.toDTO(transactionRepository.save(transaction));
    }

    @Override
    public TransactionHistoryResponse processRefund(Integer walletId, double amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new IdNotFoundException("Wallet not found"));

        wallet.setBalance(wallet.getBalance() + (amount));
        walletRepository.save(wallet);
        Transaction transaction = Transaction.builder()
                .amount(amount)
                .remaining(wallet.getBalance())
                .type(TransactionType.REFUND)
                .wallet(wallet)
                .build();
        return TransactionMapper.INSTANCE.toDTO(transactionRepository.save(transaction));
    }

    @Override
    public TransactionHistoryResponse transferMoney(Integer fromWalletId, Integer toWalletId, double amount) {
        Wallet fromWallet = walletRepository.findById(fromWalletId)
                .orElseThrow(() -> new IdNotFoundException("Sender wallet not found"));
        Wallet toWallet = walletRepository.findById(toWalletId)
                .orElseThrow(() -> new IdNotFoundException("Receiver wallet not found"));

        if (fromWallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient balance");
        }

        fromWallet.setBalance(fromWallet.getBalance() - amount);
        toWallet.setBalance(toWallet.getBalance() + amount);

        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);
        Transaction transaction = Transaction.builder()
                .amount(amount)
                .remaining(fromWallet.getBalance())
                .type(TransactionType.TRANSFER)
                .wallet(fromWallet)
                .build();
        return TransactionMapper.INSTANCE.toDTO(transactionRepository.save(transaction));
    }

    @Override
    public void deleteTransactionsByOrderId(Integer orderId) {
        transactionRepository.deleteByOrderId(orderId);
    }

}
