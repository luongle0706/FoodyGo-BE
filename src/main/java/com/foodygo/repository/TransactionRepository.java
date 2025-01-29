package com.foodygo.repository;

import com.foodygo.entity.Transaction;
import com.foodygo.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findByWallet(Wallet wallet);

}
