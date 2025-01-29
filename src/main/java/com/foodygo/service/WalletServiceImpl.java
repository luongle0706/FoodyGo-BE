package com.foodygo.service;

import com.foodygo.dto.response.DeductHistory;
import com.foodygo.dto.response.TopUpHistoryDTO;
import com.foodygo.entity.Customer;
import com.foodygo.entity.Deposit;
import com.foodygo.entity.Restaurant;
import com.foodygo.entity.Wallet;
import com.foodygo.enums.WalletType;
import com.foodygo.exception.IdNotFoundException;
import com.foodygo.mapper.DepositMapper;
import com.foodygo.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final TransactionService transactionService;
    private final DepositService depositService;

    @Override
    public void initWallet(Customer customer) {
        Wallet wallet = Wallet.builder()
                .balance((double) 0)
                .customer(customer)
                .walletType(WalletType.CUSTOMER)
                .build();

        walletRepository.save(wallet);
    }

    @Override
    public void initWallet(Restaurant restaurant) {
        Wallet wallet = Wallet.builder()
                .balance((double) 0)
                .restaurant(restaurant)
                .walletType(WalletType.RESTAURANT)
                .build();

        walletRepository.save(wallet);
    }

    @Override
    public void topUpWallet(Customer customer, double amount) {
        Wallet wallet = walletRepository.findByCustomer(customer);
        if (wallet != null) {
            wallet.setBalance(wallet.getBalance() + amount);
            walletRepository.save(wallet);
        } else {
            throw new IdNotFoundException("Wallet not found");
        }
    }

    @Override
    public void topUpWallet(Restaurant restaurant, double amount) {
        Wallet wallet = walletRepository.findWalletByRestaurant(restaurant);
        if (wallet != null) {
            wallet.setBalance(wallet.getBalance() + amount);
            walletRepository.save(wallet);
        } else {
            throw new IdNotFoundException("Wallet not found");
        }
    }

    @Override
    public void deductWallet(Customer customer, double amount) {
        Wallet wallet = walletRepository.findByCustomer(customer);
        if (wallet != null) {
            wallet.setBalance(wallet.getBalance() - amount);
            walletRepository.save(wallet);
        } else {
            throw new IdNotFoundException("Wallet not found");
        }
    }

    @Override
    public void deductWallet(Restaurant restaurant, double amount) {
        Wallet wallet = walletRepository.findWalletByRestaurant(restaurant);
        if (wallet != null) {
            wallet.setBalance(wallet.getBalance() - amount);
            walletRepository.save(wallet);
        } else {
            throw new IdNotFoundException("Wallet not found");
        }
    }

    @Override
    public double getBalance(Customer customer) {
        Wallet wallet = walletRepository.findByCustomer(customer);
        if (wallet != null) {
            return wallet.getBalance();
        }
        return 0;
    }

    @Override
    public double getBalance(Restaurant restaurant) {
        Wallet wallet = walletRepository.findWalletByRestaurant(restaurant);
        if (wallet != null) {
            return wallet.getBalance();
        }
        return 0;
    }

    @Override
    public void transfer(Customer customer, Restaurant restaurant, double amount) {
        Wallet customerWallet = walletRepository.findByCustomer(customer);
        Wallet restaurantWallet = walletRepository.findWalletByRestaurant(restaurant);
        if (customerWallet != null && restaurantWallet != null) {
            customerWallet.setBalance(customerWallet.getBalance() - amount);
            restaurantWallet.setBalance(restaurantWallet.getBalance() + amount);
            walletRepository.save(customerWallet);
            walletRepository.save(restaurantWallet);
        } else {
            throw new IdNotFoundException("Wallet not found");
        }
    }

    @Override
    public void transfer(Restaurant restaurant, Customer customer, double amount) {
        Wallet customerWallet = walletRepository.findByCustomer(customer);
        Wallet restaurantWallet = walletRepository.findWalletByRestaurant(restaurant);
        if (customerWallet != null && restaurantWallet != null) {
            customerWallet.setBalance(customerWallet.getBalance() + amount);
            restaurantWallet.setBalance(restaurantWallet.getBalance() - amount);
            walletRepository.save(customerWallet);
            walletRepository.save(restaurantWallet);
        } else {
            throw new IdNotFoundException("Wallet not found");
        }
    }

    @Override
    public void transfer(Restaurant fromRestaurant, Restaurant toRestaurant, double amount) {
        Wallet fromRestaurantWallet = walletRepository.findWalletByRestaurant(fromRestaurant);
        Wallet toRestaurantWallet = walletRepository.findWalletByRestaurant(toRestaurant);
        if (fromRestaurantWallet != null && toRestaurantWallet != null) {
            fromRestaurantWallet.setBalance(fromRestaurantWallet.getBalance() - amount);
            toRestaurantWallet.setBalance(toRestaurantWallet.getBalance() + amount);
            walletRepository.save(fromRestaurantWallet);
            walletRepository.save(toRestaurantWallet);
        } else {
            throw new IdNotFoundException("Wallet not found");
        }
    }

    @Override
    public void transfer(Customer fromCustomer, Customer toCustomer, double amount) {
        Wallet fromCustomerWallet = walletRepository.findByCustomer(fromCustomer);
        Wallet toCustomerWallet = walletRepository.findByCustomer(toCustomer);
        if (fromCustomerWallet != null && toCustomerWallet != null) {
            fromCustomerWallet.setBalance(fromCustomerWallet.getBalance() - amount);
            toCustomerWallet.setBalance(toCustomerWallet.getBalance() + amount);
            walletRepository.save(fromCustomerWallet);
            walletRepository.save(toCustomerWallet);
        } else {
            throw new IdNotFoundException("Wallet not found");
        }
    }

    @Override
    public List<TopUpHistoryDTO> getTopUpHistory(Customer customer) {
        List<Deposit> historyDeposits = depositService.getDepositByCustomer(customer);
        List<TopUpHistoryDTO> topUpHistory = historyDeposits.stream()
                .map(DepositMapper.INSTANCE::toTopUpHistoryDTO)
                .collect(Collectors.toList());
        return topUpHistory;
    }

    @Override
    public List<TopUpHistoryDTO> getTopUpHistory(Restaurant restaurant) {
        return List.of();
    }

    @Override
    public List<DeductHistory> getDeductHistory(Customer customer) {
        return List.of();
    }

    @Override
    public List<DeductHistory> getDeductHistory(Restaurant restaurant) {
        return List.of();
    }
}
