package com.foodygo.repository;

import com.foodygo.entity.Customer;
import com.foodygo.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Integer> {

    List<Deposit> findDepositsByCustomer(Customer customer);

}
