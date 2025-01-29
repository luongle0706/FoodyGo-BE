package com.foodygo.service;

import com.foodygo.entity.Customer;
import com.foodygo.entity.Deposit;
import com.foodygo.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepositServiceImpl implements DepositService {

    private final DepositRepository depositRepository;

    @Override
    public Deposit createDeposit(Deposit deposit) {
        return depositRepository.save(deposit);
    }

    @Override
    public Deposit getDepositById(Integer id) {
        return depositRepository.findById(id).orElse(null);
    }

    @Override
    public List<Deposit> getDepositByCustomer(Customer customer) {
        return depositRepository.findDepositsByCustomer(customer);
    }

}
