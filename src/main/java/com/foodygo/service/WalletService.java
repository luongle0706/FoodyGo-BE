package com.foodygo.service;

import com.foodygo.dto.response.DeductHistory;
import com.foodygo.dto.response.TopUpHistoryDTO;
import com.foodygo.entity.Customer;
import com.foodygo.entity.Restaurant;

import java.util.List;

public interface WalletService {

    void initWallet(Customer customer);
    void initWallet(Restaurant restaurant);
    void topUpWallet(Customer customer, double amount);
    void topUpWallet(Restaurant restaurant, double amount);
    void deductWallet(Customer customer, double amount);
    void deductWallet(Restaurant restaurant, double amount);
    double getBalance(Customer customer);
    double getBalance(Restaurant restaurant);
    void transfer(Customer customer, Restaurant restaurant, double amount);
    void transfer(Restaurant restaurant, Customer customer, double amount);
    void transfer(Restaurant fromRestaurant, Restaurant toRestaurant, double amount);
    void transfer(Customer fromCustomer, Customer toCustomer, double amount);
    List<TopUpHistoryDTO> getTopUpHistory(Customer customer);
    List<TopUpHistoryDTO> getTopUpHistory(Restaurant restaurant);
    List<DeductHistory> getDeductHistory(Customer customer);
    List<DeductHistory> getDeductHistory(Restaurant restaurant);

}
