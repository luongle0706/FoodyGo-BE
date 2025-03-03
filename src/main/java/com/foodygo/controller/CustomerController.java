package com.foodygo.controller;

import com.foodygo.dto.BuildingDTO;
import com.foodygo.dto.CustomerDTO;
import com.foodygo.dto.UserDTO;
import com.foodygo.dto.request.CustomerCreateRequest;
import com.foodygo.dto.request.CustomerUpdateRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.dto.response.PagingResponse;
import com.foodygo.entity.*;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.exception.UnchangedStateException;
import com.foodygo.service.spec.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
@Tag(name = "Customer", description = "Operations related to Customer management")
public class CustomerController {

    private final CustomerService customerService;

    @Value("${application.default-current-page}")
    private int defaultCurrentPage;

    @Value("${application.default-page-size}")
    private int defaultPageSize;

//    /**
//     * Method get all customers
//     *
//     * @param currentPage currentOfThePage
//     * @param pageSize numberOfElement
//     * @return list or empty
//     */
//    @Operation(summary = "Get all customers", description = "Retrieves all customers, with optional pagination")
//    @PreAuthorize("hasRole('MANAGER')")
//    @GetMapping("")
//    public ResponseEntity<PagingResponse> getAllCustomers(
//            @RequestParam(value = "currentPage", required = false) Integer currentPage,
//            @RequestParam(value = "pageSize", required = false) Integer pageSize,
//            @RequestParam(value = "status", required = false) String status) {
//
//        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
//        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
//        PagingResponse results = (status != null && status.equals("active"))
//                ? customerService.getAllCustomersActive(resolvedCurrentPage, resolvedPageSize)
//                : customerService.getAllCustomers(resolvedCurrentPage, resolvedPageSize);
//        List<?> data = (List<?>) results.getData();
//        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
//    }

    /**
     * Method get all customers
     *
     * @param currentPage currentOfThePage
     * @param pageSize numberOfElement
     * @return list or empty
     */
    @Operation(summary = "Get all customers", description = "Retrieves all customers, with optional pagination")
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("")
    public ResponseEntity<PagingResponse> getAllCustomers(@RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                          @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
        PagingResponse results = customerService.getAllCustomers(resolvedCurrentPage, resolvedPageSize);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

    /**
     * Method get all customers have status is active
     *
     * @param currentPage currentOfThePage
     * @param pageSize numberOfElement
     * @return list or empty
     */
    @Operation(summary = "Get all customers active", description = "Retrieves all customers have status is active, with optional pagination")
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/active")
    public ResponseEntity<PagingResponse> getAllCustomersActive(@RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                                @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
        PagingResponse results = customerService.getAllCustomersActive(resolvedCurrentPage, resolvedPageSize);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

//    // lấy tất cả các order từ customer id
//    /**
//     * Method get all customers have status is active
//     *
//     * @param currentPage currentOfThePage
//     * @param pageSize numberOfElement
//     * @return list or empty
//     */
//    @Operation(summary = "Get all customers active", description = "Retrieves all customers have status is active, with optional pagination")
//    @PreAuthorize("hasRole('MANAGER') or hasRole('STAFF')")
//    @GetMapping("/get-all-orders/{customer-id}")
//    public ResponseEntity<ObjectResponse> getOrdersByCustomerID(@PathVariable("customer-id") int customerID) {
//        List<Order> results = customerService.getOrdersByCustomerID(customerID);
//        return results != null ?
//                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all orders by customer ID successfully", results)) :
//                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get all orders by customer ID failed", null));
//    }

//    // lấy customer từ order id
//    /**
//     * Method get all customers have status is active
//     *
//     * @param currentPage currentOfThePage
//     * @param pageSize numberOfElement
//     * @return list or empty
//     */
//    @Operation(summary = "Get all customers active", description = "Retrieves all customers have status is active, with optional pagination")
//    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('STAFF')")
//    @GetMapping("/get-customer/{order-id}")
//    public ResponseEntity<ObjectResponse> getCustomerByOrderID(@PathVariable("order-id") int orderID) {
//        CustomerDTO results = customerService.getCustomerByOrderID(orderID);
//        return results != null ?
//                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get customer by order ID successfully", results)) :
//                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get customer by order ID failed", null));
//    }

    /**
     * Method get building by customer id
     *
     * @param customerID idOfCustomer
     * @return building or null
     */
    @Operation(summary = "Get building by customer id", description = "Retrieves building by customer id")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('STAFF')")
    @GetMapping("/{customer-id}/building")
    public ResponseEntity<ObjectResponse> getBuildingByCustomerID(@PathVariable("customer-id") int customerID) {
        BuildingDTO results = customerService.getBuildingByCustomerID(customerID);
        return results != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get building by customer ID successfully", results)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get building by customer ID failed", null));
    }

    /**
     * Method get user by customer id
     *
     * @param customerID idOfCustomer
     * @return user or null
     */
    @Operation(summary = "Get user by customer id", description = "Retrieves user by customer id")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('STAFF')")
    @GetMapping("/{customer-id}/user")
    public ResponseEntity<ObjectResponse> getUserByCustomerID(@PathVariable("customer-id") int customerID) {
        UserDTO results = customerService.getUserByCustomerID(customerID);
        return results != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get user by customer ID successfully", results)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get user by customer ID failed", null));
    }

    /**
     * Method get wallet by customer id
     *
     * @param customerID idOfCustomer
     * @return wallet or null
     */
    @Operation(summary = "Get wallet by customer id", description = "Retrieves wallet by customer id")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER')")
    @GetMapping("/{customer-id}/wallet")
    public ResponseEntity<ObjectResponse> getWalletByCustomerID(@PathVariable("customer-id") int customerID) {
        Wallet results = customerService.getWalletByCustomerID(customerID);
        return results != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get wallet by customer ID successfully", results)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get wallet by customer ID failed", null));
    }

//    // lấy user từ wallet id
//    /**
//     * Method get wallet by customer id
//     *
//     * @param customerID idOfCustomer
//     * @return user or null
//     */
//    @Operation(summary = "Get wallet by customer id", description = "Retrieves wallet by customer id")
//    @PreAuthorize("hasRole('USER') or hasRole('MANAGER')")
//    @GetMapping("/get-user-by-wallet/{wallet-id}")
//    public ResponseEntity<ObjectResponse> getCustomerByWalletID(@PathVariable("wallet-id") int walletID) {
//        CustomerDTO results = customerService.getCustomerByWalletID(walletID);
//        return results != null ?
//                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get customer by wallet ID successfully", results)) :
//                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get customer by wallet ID failed", null));
//    }

    /**
     * Method restore customer by customer id and set deleted = false
     *
     * @param customerID idOfCustomer
     * @return customer or null
     */
    @Operation(summary = "restore customer by customer id", description = "restore customer by customer id and set deleted = false")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{customer-id}/restore")
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

    /**
     * Method get customer by customer id
     *
     * @param customerID idOfCustomer
     * @return customer or null
     */
    @Operation(summary = "get customer by customer id", description = "get customer by customer id")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('STAFF') or hasRole('SELLER')")
    @GetMapping("/{customer-id}")
    public ResponseEntity<ObjectResponse> getCustomerByID(@PathVariable("customer-id") int customerID) {
        Customer customer = customerService.findById(customerID);
        return customer != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get customer by ID successfully", customer)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get customer by ID failed", null));
    }

    /**
     * Method create customer
     *
     * @param customerCreateRequest param basic for save customer
     * @return customer or null
     */
    @Operation(summary = "Create customer", description = "Create customer")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<ObjectResponse> createCustomer(@Valid @RequestBody CustomerCreateRequest customerCreateRequest) {
        try {
            CustomerDTO customer = customerService.createCustomer(customerCreateRequest);
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

    /**
     * Method update customer by id
     *
     * @param customerID idOfCustomer
     * @param customerUpdateRequest param basic for save customer
     * @return customer or null
     */
    @Operation(summary = "Update customer by id", description = "Update customer by id")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
//    @PostAuthorize("returnObject.customer.id == customerID")
    @PutMapping("/{customer-id}")
    public ResponseEntity<ObjectResponse> updateCustomer(@PathVariable("customer-id") int customerID, @RequestBody CustomerUpdateRequest customerUpdateRequest) {
        try {
            CustomerDTO customer = customerService.updateCustomer(customerUpdateRequest, customerID);
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

    /**
     * Method delete customer by id and set deleted = true and set delete of user = true, enabled = false and nonLocked = false
     *
     * @param customerID idOfCustomer
     * @return customer or null
     */
    @Operation(summary = "Delete customer by id", description = "delete customer by id and set deleted = true and set delete of user = true, enabled = false and nonLocked = false")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{customer-id}")
    public ResponseEntity<ObjectResponse> deleteCustomerByID(@PathVariable("customer-id") int customerID) {
        try {
            CustomerDTO customer = customerService.deleteCustomer(customerID);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Delete customer successfully", customer));
        } catch (ElementNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Delete customer ailed. " + e.getMessage(), null));
        } catch (UnchangedStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Delete customer failed. " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Delete customer failed", null));
        }
    }

}
