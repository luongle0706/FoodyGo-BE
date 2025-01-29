package com.foodygo.service;

import com.foodygo.entity.Transaction;
import com.foodygo.entity.Wallet;

import java.util.List;

public interface TransactionService {

    Transaction createTransaction(Transaction transaction);
    Transaction getTransactionById(Integer id);
    List<Transaction> getTransactionByWallet(Wallet wallet);

}
