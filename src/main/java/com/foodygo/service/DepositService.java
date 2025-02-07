package com.foodygo.service;

import com.foodygo.entity.Deposit;


public interface DepositService {

    Deposit requestDeposit(Deposit deposit);
    void approveDeposit(Integer depositId);

}
