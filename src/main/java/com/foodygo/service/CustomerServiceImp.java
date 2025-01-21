package com.foodygo.service;

import com.foodygo.dto.request.CustomerCreateRequest;
import com.foodygo.dto.request.CustomerUpdateRequest;
import com.foodygo.entity.*;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImp extends BaseServiceImp<Customer, Integer> implements CustomerService {

    private final CustomerRepository customerRepository;
    private final BuildingService buildingService;
    private final UserService userService;

    public CustomerServiceImp(CustomerRepository customerRepository, BuildingService buildingService, UserService userService) {
        super(customerRepository);
        this.customerRepository = customerRepository;
        this.buildingService = buildingService;
        this.userService = userService;
    }


    @Override
    public List<Customer> getAllCustomersActive() {
        List<Customer> customers = customerRepository.findAll();
        List<Customer> activeCustomers = new ArrayList<Customer>();
        for (Customer customer : customers) {
            if(!customer.isDeleted()) {
                activeCustomers.add(customer);
            }
        }
        return activeCustomers;
    }

    @Override
    public Customer undeleteCustomer(Integer customerID) {
        Customer customer = customerRepository.findCustomerById((customerID));
        if (customer != null) {
            if (customer.isDeleted()) {
                customer.setDeleted(false);
                return customerRepository.save(customer);
            }
        }
        return null;
    }

    @Override
    public User getUserByCustomerID(Integer customerID) {
        Customer customer = customerRepository.findCustomerById(customerID);
        if (customer != null) {
            return customer.getUser();
        }
        return null;
    }

    private Customer getCustomer(Integer customerID) {
        Customer customer = customerRepository.findCustomerById(customerID);
        if (customer == null) {
            throw new ElementNotFoundException("Customer not found");
        }
        return customer;
    }

    private Building getBuilding(int buildingID) {
        Building building = buildingService.findById(buildingID);
        if (building == null) {
            throw new ElementNotFoundException("Building is not found");
        }
        return building;
    }

    private User getUser(int userID) {
        User user = userService.findById(userID);
        if (user == null) {
            throw new ElementNotFoundException("User is not found");
        }
        return user;
    }

    @Override
    public Customer createCustomer(CustomerCreateRequest customerCreateRequest) {
        Building building = getBuilding(customerCreateRequest.getBuildingID());
        User user = getUser(customerCreateRequest.getUserID());
        Customer customer = Customer.builder()
                .image(customerCreateRequest.getImage())
                .building(building)
                .user(user)
                .build();
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(CustomerUpdateRequest customerUpdateRequest, int customerID) {
        Customer customer = getCustomer(customerID);
        if (customer != null) {
            if(customerUpdateRequest.getImage() != null) {
                customer.setImage(customerUpdateRequest.getImage());
            }
            if (customerUpdateRequest.getBuildingID() > 0) {
                Building building = getBuilding(customerUpdateRequest.getBuildingID());
                customer.setBuilding(building);
            }
            if (customerUpdateRequest.getUserID() > 0) {
                User user = getUser(customerUpdateRequest.getUserID());
                customer.setUser(user);
            }
            return customerRepository.save(customer);
        }
        return null;
    }

    @Override
    public List<Order> getOrdersByCustomerID(Integer customerID) {
        // chua có order service
        return List.of();
    }

    @Override
    public Customer getCustomerByOrderID(Integer orderID) {
        // chua có order service
        return null;
    }

    @Override
    public Building getBuildingByCustomerID(int customerID) {
        Customer customer = getCustomer(customerID);
        if (customer != null) {
            return customer.getBuilding();
        }
        return null;
    }

    @Override
    public User getUserByCustomerID(int customerID) {
        Customer customer = getCustomer(customerID);
        if (customer != null) {
            return customer.getUser();
        }
        return null;
    }

    @Override
    public Wallet getWalletByCustomerID(Integer customerID) {
        Customer customer = getCustomer(customerID);
        if (customer != null) {
            return customer.getWallet();
        }
        return null;
    }

    @Override
    public Customer getCustomerByWalletID(Integer walletID) {
        // chưa có wallet service
        return null;
    }

//    @Override
//    public List<Deposit> getDepositByCustomerID(Integer customerID) {
//        Customer customer = getCustomer(customerID);
//        if (customer != null) {
//            return customer.getDeposit();
//        }
//        return null;
//    }
//
//    @Override
//    public Customer getCustomerByDepositID(Integer depositID) {
//        // chưa có wallet service
//        return null;
//    }

}
