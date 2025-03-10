package com.foodygo.service.impl;
import com.foodygo.dto.response.TransactionHistoryResponse;
import com.foodygo.enums.TransactionType;
import com.foodygo.mapper.TransactionMapper;
import com.foodygo.repository.TransactionRepository;
import com.foodygo.repository.WalletRepository;
import com.foodygo.service.spec.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    @Override
    public List<TransactionHistoryResponse> getTransactionsByWallet(Integer walletId) {
        return TransactionMapper.INSTANCE.toDTO(transactionRepository.findByWalletId(walletId));
    }

    @Override
    public List<TransactionHistoryResponse> getTransactionByType(Integer walletId) {
        return TransactionMapper.INSTANCE.toDTO(transactionRepository.findByWalletIdAndType(walletId, TransactionType.PAYMENT));
    }

    @Override
    public void deleteTransactionsByOrderId(Integer orderId) {
        transactionRepository.deleteByOrderId(orderId);
    }

}
