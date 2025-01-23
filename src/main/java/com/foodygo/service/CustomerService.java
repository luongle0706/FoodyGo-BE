package com.foodygo.service;

import com.foodygo.dto.CustomerDTO;
import com.foodygo.dto.UserDTO;
import com.foodygo.dto.request.CustomerCreateRequest;
import com.foodygo.dto.request.CustomerUpdateRequest;
import com.foodygo.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService extends BaseService<Customer, Integer> {
    List<Customer> getAllCustomersActive();

    CustomerDTO undeleteCustomer(Integer customerID);

    UserDTO getUserByCustomerID(Integer customerID);

    CustomerDTO createCustomer(CustomerCreateRequest customerCreateRequest);

    CustomerDTO updateCustomer(CustomerUpdateRequest customerUpdateRequest, int customerID);

    List<Order> getOrdersByCustomerID(Integer customerID);

    CustomerDTO getCustomerByOrderID(Integer orderID);

    Building getBuildingByCustomerID(int customerID);

    User getUserByCustomerID(int customerID);

    Wallet getWalletByCustomerID(Integer customerID);

    CustomerDTO getCustomerByWalletID(Integer walletID);

    CustomerDTO deleteCustomer(Integer customerID);

//    List<Deposit> getDepositByCustomerID(Integer customerID);
//
//    Customer getCustomerByDepositID(Integer depositID);

}
