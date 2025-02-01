package com.foodygo.repository;

import com.foodygo.entity.Transaction;
import com.foodygo.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findByWallet(Wallet wallet);

    @Modifying
    @Query("DELETE FROM Transaction ts WHERE ts.order.id = :orderId")
    void deleteByOrderId(@Param("orderId") Integer orderId);
}
