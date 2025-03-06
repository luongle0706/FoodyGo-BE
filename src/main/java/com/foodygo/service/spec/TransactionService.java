package com.foodygo.service.spec;

import com.foodygo.dto.response.TransactionHistoryResponse;
import com.foodygo.enums.TransactionType;

import java.util.List;

public interface TransactionService {

    List<TransactionHistoryResponse> getTransactionsByWallet(Integer walletId);
    List<TransactionHistoryResponse> getTransactionByType(Integer walletId);
    void deleteTransactionsByOrderId(Integer orderId);
}
