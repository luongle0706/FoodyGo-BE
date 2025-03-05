package com.foodygo.service.spec;

import com.foodygo.entity.Deposit;


public interface DepositService {

    Deposit requestDeposit(Deposit deposit);
    void approveDeposit(Integer depositId);

}
