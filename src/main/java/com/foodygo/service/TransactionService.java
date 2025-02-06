package com.foodygo.service;

import com.foodygo.dto.response.TransactionHistoryResponse;
import com.foodygo.entity.Transaction;
import com.foodygo.entity.Wallet;
import com.foodygo.enums.TransactionType;
import com.foodygo.exception.IdNotFoundException;

import java.util.List;

public interface TransactionService {

    List<TransactionHistoryResponse> getTransactionsByWallet(Integer walletId);
    TransactionHistoryResponse getTransactionById(Integer transactionId);
    List<TransactionHistoryResponse> getTransactionsByType(Integer walletId, TransactionType type);
    TransactionHistoryResponse processPayment(Integer walletId, double amount);
    TransactionHistoryResponse processRefund(Integer walletId, double amount);
    TransactionHistoryResponse transferMoney(Integer fromWalletId, Integer toWalletId, double amount);
    void deleteTransactionsByOrderId(Integer orderId);
}
