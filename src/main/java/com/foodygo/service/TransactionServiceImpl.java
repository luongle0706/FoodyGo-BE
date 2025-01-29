package com.foodygo.service;
import com.foodygo.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    @Override
    public Transaction createTransaction(Transaction transaction) {
        return null;
    }

    @Override
    public Transaction getTransactionById(Integer id) {
        return null;
    }

    @Override
    public List<Transaction> getTransactionByCustomerId(Integer customerId) {
        return null;
    }

    @Override
    public List<Transaction> getTransactionByRestaurantId(Integer restaurantId) {
        return null;
    }



}
