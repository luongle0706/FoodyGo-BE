package com.foodygo.controller;

import com.foodygo.dto.CustomerDTO;
import com.foodygo.dto.request.CustomerCreateRequest;
import com.foodygo.dto.request.CustomerUpdateRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.dto.response.PagingResponse;
import com.foodygo.entity.*;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.exception.UnchangedStateException;
import com.foodygo.service.CustomerService;
import com.foodygo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final UserService userService;

    @Value("${application.default-current-page}")
    private int defaultCurrentPage;

    @Value("${application.default-page-size}")
    private int defaultPageSize;

    // lấy tất cả các customer
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all")
    public ResponseEntity<PagingResponse> getAllCustomers(@RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                          @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
        PagingResponse results = customerService.findAll(resolvedCurrentPage, resolvedPageSize);
        return ResponseEntity.status(HttpStatus.OK).body(results);
    }

    // lấy tất cả các order từ customer id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all-orders/{customer-id}")
    public ResponseEntity<ObjectResponse> getOrdersByCustomerID(@PathVariable("customer-id") int customerID) {
        List<Order> results = customerService.getOrdersByCustomerID(customerID);
        return results != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all orders by customer ID successfully", results)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get all orders by customer ID failed", null));
    }

    // lấy customer từ order id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-customer/{order-id}")
    public ResponseEntity<ObjectResponse> getCustomerByOrderID(@PathVariable("order-id") int orderID) {
        Customer results = customerService.getCustomerByOrderID(orderID);
        return results != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get customer by order ID successfully", results)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get customer by order ID failed", null));
    }

    // lấy building từ customer id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-building/{customer-id}")
    public ResponseEntity<ObjectResponse> getBuildingByCustomerID(@PathVariable("customer-id") int customerID) {
        Building results = customerService.getBuildingByCustomerID(customerID);
        return results != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get building by customer ID successfully", results)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get building by customer ID failed", null));
    }

    // lấy user từ customer id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-user/{customer-id}")
    public ResponseEntity<ObjectResponse> getUserByCustomerID(@PathVariable("customer-id") int customerID) {
        User results = customerService.getUserByCustomerID(customerID);
        return results != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get user by customer ID successfully", results)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get user by customer ID failed", null));
    }

    // lấy wallet từ customer id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-wallet/{customer-id}")
    public ResponseEntity<ObjectResponse> getWalletByCustomerID(@PathVariable("customer-id") int customerID) {
        Wallet results = customerService.getWalletByCustomerID(customerID);
        return results != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get wallet by customer ID successfully", results)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get wallet by customer ID failed", null));
    }

    // lấy user từ wallet id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-user/{wallet-id}")
    public ResponseEntity<ObjectResponse> getCustomerByWalletID(@PathVariable("wallet-id") int walletID) {
        Customer results = customerService.getCustomerByWalletID(walletID);
        return results != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get customer by wallet ID successfully", results)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get customer by wallet ID failed", null));
    }

    // lấy tất cả các customers chưa bị xóa
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all-active")
    public ResponseEntity<ObjectResponse> getAllCustomersActive() {
        List<Customer> results = customerService.getAllCustomersActive();
        return !results.isEmpty() ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all customers active successfully", results)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get all customers active failed", null));
    }

    // khôi phục lại customer đó
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/undelete/{customer-id}")
    public ResponseEntity<ObjectResponse> unDeleteCustomerByID(@PathVariable("customer-id") int customerID) {
        try {
            CustomerDTO customerDTO = customerService.undeleteCustomer(customerID);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Undelete customer successfully", customerDTO));
        } catch (ElementNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Undelete customer failed. " + e.getMessage(), null));
        } catch (UnchangedStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Undelete customer failed. " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Undelete customer failed", null));
        }
    }

    // lấy ra customer bằng id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get/{customer-id}")
    public ResponseEntity<ObjectResponse> getCustomerByID(@PathVariable("customer-id") int customerID) {
        Customer customer = customerService.findById(customerID);
        return customer != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get customer by ID successfully", customer)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get customer by ID failed", null));
    }

    // tạo ra customer
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<ObjectResponse> createCustomer(@Valid @RequestBody CustomerCreateRequest customerCreateRequest) {
        try {
            Customer customer = customerService.createCustomer(customerCreateRequest);
            if (customer != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Create customer successfully", customer));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Create customer failed. Customer is null.", null));
        } catch (ElementNotFoundException e) {
            log.error("Error while creating customer", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Create customer failed. " + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error creating customer", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Create customer failed", null));
        }
    }

    // update customer bằng id
    @PreAuthorize("hasRole('USER')")
//    @PostAuthorize("returnObject.customer.id == customerID")
    @PutMapping("/update/{customer-id}")
    public ResponseEntity<ObjectResponse> updateCustomer(@PathVariable("customer-id") int customerID, @RequestBody CustomerUpdateRequest customerUpdateRequest) {
        try {
            Customer customer = customerService.updateCustomer(customerUpdateRequest, customerID);
            if (customer != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Update customer successfully", customer));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Update customer failed. Customer is null", null));
        } catch (ElementNotFoundException e) {
            log.error("Error while updating customer", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Update customer failed. " + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error updating customer", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Update customer failed", null));
        }
    }

    // xóa customer, tức set deleted = true và xóa luôn user
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{customer-id}")
    public ResponseEntity<ObjectResponse> deleteCustomerByID(@PathVariable("customer-id") int customerID) {
        try {
            Customer customer = customerService.findById(customerID);
            User user = customer.getUser();
            if(customer != null) {
                customer.setDeleted(true);
                if(user != null) {
                    user.setDeleted(true);
                    user.setEnabled(false);
                    user.setNonLocked(false);
                    userService.save(user);
                }
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Delete customer successfully", customerService.save(customer)));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Delete customer failed", null));
        } catch (Exception e) {
            log.error("Error deleting customer", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Delete customer failed", null));
        }
    }

}
