package com.foodygo.repository;

import com.foodygo.entity.Customer;
import com.foodygo.entity.Restaurant;
import com.foodygo.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    Wallet findByCustomer(Customer customer);

    Wallet findWalletByRestaurant(Restaurant restaurant);

}
