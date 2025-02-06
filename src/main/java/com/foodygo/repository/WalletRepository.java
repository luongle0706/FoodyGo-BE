package com.foodygo.repository;

import com.foodygo.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    Optional<Wallet> findByCustomerId(Integer customerId);
    Optional<Wallet> findByRestaurantId(Integer restaurantId);
}
