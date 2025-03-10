package com.foodygo.service.impl;

import com.foodygo.dto.response.WalletBalanceResponse;
import com.foodygo.entity.Order;
import com.foodygo.entity.Transaction;
import com.foodygo.entity.User;
import com.foodygo.entity.Wallet;
import com.foodygo.enums.DepositMethod;
import com.foodygo.enums.TransactionType;
import com.foodygo.exception.BadRequestException;
import com.foodygo.exception.IdNotFoundException;
import com.foodygo.repository.TransactionRepository;
import com.foodygo.repository.UserRepository;
import com.foodygo.repository.WalletRepository;
import com.foodygo.service.spec.WalletService;
import com.foodygo.thirdparty.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final PaymentService paymentService;

    @Override
    public WalletBalanceResponse getWalletByCustomerId(Integer customerId) {
        Wallet wallet = walletRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new IdNotFoundException("Wallet not found for customer: " + customerId));
        return WalletBalanceResponse.builder()
                .id(wallet.getId())
                .fullName(wallet.getCustomer().getUser().getFullName())
                .balance(wallet.getBalance())
                .build();
    }

    @Override
    public void processWithdraw(Integer walletId, Double amount) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new IdNotFoundException("Cannot found the wallet with id: " + walletId));
        if (amount <= 0) {
            throw new IdNotFoundException("Number of foodyxu must be more than 0");
        }
        if (wallet.getBalance() <= amount) {
            throw new IdNotFoundException("The balance is not enough to withdraw");
        }
        Transaction transaction = Transaction.builder()
                .description("Rút tiền từ ví")
                .amount(amount )
                .remaining(wallet.getBalance() - amount)
                .type(TransactionType.WITHDRAWAL)
                .wallet(wallet)
                .build();
        wallet.setBalance(wallet.getBalance() - amount );
        transactionRepository.save(transaction);
        walletRepository.save(wallet);
    }

    @Override
    public void processTransfer(Integer walletId, String receiver, Integer amount, String note) {
        // Validate sender wallet
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new IdNotFoundException("Cannot found wallet with id: " + walletId));

        if (wallet.getBalance() < amount) {
            throw new IdNotFoundException("Cannot transfer because balance is not enough");
        }

        // Validate receiver exists
        User user = userRepository.getUserByPhone(receiver);
        if (user == null) {
            throw new IdNotFoundException("Cannot found receiver with phone: " + receiver);
        }

        // Validate receiver has customer profile
        if (user.getCustomer() == null) {
            throw new IdNotFoundException("The receiver does not have a customer profile");
        }

        // Validate receiver has wallet
        Wallet receiverWallet = user.getCustomer().getWallet();
        if (receiverWallet == null) {
            throw new IdNotFoundException("The receiver does not have a wallet");
        }

        // Create transaction records
        Transaction transactionOfSender = Transaction.builder()
                .description("Chuyển tiền cho " + receiver + " :" + note)
                .amount(Double.valueOf(amount))
                .remaining(wallet.getBalance() - amount)
                .type(TransactionType.TRANSFER)
                .wallet(wallet)
                .build();

        Transaction transactionOfReceiver = Transaction.builder()
                .description("Nhận tiền từ " + wallet.getCustomer().getUser().getPhone() + " :" + note)
                .amount(Double.valueOf(amount))
                .remaining(receiverWallet.getBalance() + amount)
                .type(TransactionType.TRANSFER)
                .wallet(receiverWallet) // Fix: This should point to receiverWallet, not sender's wallet
                .build();

        // Update balances
        receiverWallet.setBalance(receiverWallet.getBalance() + amount);
        wallet.setBalance(wallet.getBalance() - amount);

        // Save changes
        walletRepository.save(wallet);
        walletRepository.save(receiverWallet);
        transactionRepository.save(transactionOfSender);
        transactionRepository.save(transactionOfReceiver);
    }

    @Override
    public Object processTopUp(Integer walletId, Integer amount, String method, HttpServletRequest request) {
        // Validate wallet and amount
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new IdNotFoundException("Cannot found wallet with id: " + walletId));

        if (amount <= 0) {
            throw new IdNotFoundException("Number of foodyxu must be more than 0");
        }

        return paymentService.requestPayment(amount,"NCB", request, wallet.getCustomer().getId());
    }

    @Override // The Anh da~ xai ham nay nha Loc PT
    public void paymentOrder(Order order) {
        Wallet customerWallet = order.getCustomer().getWallet();
        Wallet restaurantWallet = order.getRestaurant().getWallet();

        if (customerWallet.getBalance() < order.getTotalPrice()) {
            throw new BadRequestException("Cannot payment because the balance not enough to pay order");
        }

        Transaction customerTransaction = Transaction.builder()
                .description("Thanh toán đơn hàng: " + order.getId())
                .amount(order.getTotalPrice())
                .remaining(customerWallet.getBalance() - order.getTotalPrice())
                .type(TransactionType.PAYMENT)
                .order(order)
                .wallet(customerWallet)
                .build();
        customerWallet.setBalance(customerWallet.getBalance() - order.getTotalPrice());

        Transaction restaurantTransaction = Transaction.builder()
                .description("Nhận tiền từ đơn hàng: " + order.getId())
                .amount(order.getTotalPrice())
                .remaining(restaurantWallet.getBalance() + order.getTotalPrice())
                .type(TransactionType.PAYMENT)
                .order(order)
                .wallet(customerWallet)
                .build();

        restaurantWallet.setBalance(restaurantWallet.getBalance() + order.getTotalPrice());

        walletRepository.saveAll(List.of(customerWallet, restaurantWallet));
        transactionRepository.saveAll(List.of(customerTransaction, restaurantTransaction));
    }

    private DepositMethod getDepositMethod(String method) {
        return switch (method.toUpperCase()) {
            case "VNPAY" -> DepositMethod.VNPAY;
            case "MOMO" -> DepositMethod.MOMO;
            default -> null;
        };
    }



}