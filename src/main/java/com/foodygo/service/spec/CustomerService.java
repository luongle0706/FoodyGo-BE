package com.foodygo.service.spec;

import com.foodygo.dto.BuildingDTO;
import com.foodygo.dto.CustomerDTO;
import com.foodygo.dto.HubDTO;
import com.foodygo.dto.UserDTO;
import com.foodygo.dto.request.CustomerCreateRequest;
import com.foodygo.dto.request.CustomerUpdateRequest;
import com.foodygo.dto.response.PagingResponse;
import com.foodygo.entity.*;

import java.util.List;

public interface CustomerService extends BaseService<Customer, Integer> {
    PagingResponse getAllCustomers(Integer currentPage, Integer pageSize);

    PagingResponse getAllCustomersActive(Integer currentPage, Integer pageSize);

    List<CustomerDTO> getCustomers();

    CustomerDTO undeleteCustomer(Integer customerID);

    UserDTO getUserByCustomerID(Integer customerID);

    CustomerDTO createCustomer(CustomerCreateRequest customerCreateRequest);

    UserDTO updateCustomer(CustomerUpdateRequest customerUpdateRequest, int userId);

    List<Order> getOrdersByCustomerID(Integer customerID);

    CustomerDTO getCustomerByOrderID(Integer orderID);

    BuildingDTO getBuildingByCustomerID(int customerID);

    UserDTO getUserByCustomerID(int customerID);

    Wallet getWalletByCustomerID(Integer customerID);

    CustomerDTO getCustomerByWalletID(Integer walletID);

    CustomerDTO deleteCustomer(Integer customerID);

//    List<Deposit> getDepositByCustomerID(Integer customerID);
//
//    Customer getCustomerByDepositID(Integer depositID);

}
