package com.foodygo.service;

import com.foodygo.entity.Customer;
import com.foodygo.entity.Deposit;

import java.util.List;

public interface DepositService {

    Deposit createDeposit(Deposit deposit);
    Deposit getDepositById(Integer id);
    List<Deposit> getDepositByCustomer(Customer customer);

}
