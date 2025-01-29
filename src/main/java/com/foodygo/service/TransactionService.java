package com.foodygo.service;

import com.foodygo.entity.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction createTransaction(Transaction transaction);
    Transaction getTransactionById(Integer id);
    List<Transaction> getTransactionByCustomerId(Integer customerId);
    List<Transaction> getTransactionByRestaurantId(Integer restaurantId);

}
