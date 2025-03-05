package com.foodygo.service.impl;

import com.foodygo.entity.Deposit;
import com.foodygo.entity.Transaction;
import com.foodygo.entity.Wallet;
import com.foodygo.enums.TransactionType;
import com.foodygo.exception.IdNotFoundException;
import com.foodygo.repository.DepositRepository;
import com.foodygo.repository.TransactionRepository;
import com.foodygo.repository.WalletRepository;
import com.foodygo.service.spec.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepositServiceImpl implements DepositService {

    private final DepositRepository depositRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public Deposit requestDeposit(Deposit deposit) {
        return depositRepository.save(deposit);
    }

    @Override
    public void approveDeposit(Integer depositId) {
        Deposit deposit = depositRepository.findById(depositId)
                .orElseThrow(() -> new IdNotFoundException("Deposit not found"));

        Wallet wallet = deposit.getWallet();
        wallet.setBalance(wallet.getBalance() + (deposit.getAmount()));
        walletRepository.save(wallet);
        Transaction transaction = Transaction.builder()
                .amount(deposit.getAmount())
                .remaining(wallet.getBalance())
                .type(TransactionType.TOP_UP)
                .wallet(wallet)
                .build();
        transactionRepository.save(transaction);
    }

}
