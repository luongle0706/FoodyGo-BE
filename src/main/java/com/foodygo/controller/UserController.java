package com.foodygo.controller;

import com.foodygo.dto.CustomerDTO;
import com.foodygo.dto.UserDTO;
import com.foodygo.dto.request.UserCreateRequest;
import com.foodygo.dto.request.UserUpdateRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.dto.response.PagingResponse;
import com.foodygo.entity.*;
import com.foodygo.exception.AuthenticationException;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Value("${application.default-current-page}")
    private int defaultCurrentPage;

    @Value("${application.default-page-size}")
    private int defaultPageSize;

    @GetMapping("/test")
    public String testne() {
        return "testne";
    }

    // lấy tất cả các user
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all")
    public ResponseEntity<PagingResponse> getAllUsers(@RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                          @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
        PagingResponse results = userService.findAllUsers(resolvedCurrentPage, resolvedPageSize);
        return ResponseEntity.status(HttpStatus.OK).body(results);
    }

    // update user bằng id
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{user-id}")
    public ResponseEntity<ObjectResponse> updateUser(@PathVariable("user-id") int userID, @RequestBody UserUpdateRequest userUpdateRequest) {
        try {
            UserDTO user = userService.updateUser(userUpdateRequest, userID);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Update user successfully", user));
        } catch (AuthenticationException e) {
            log.error("Error while updating user", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ObjectResponse("Fail", "Update user failed. " + e.getMessage(), null));
        } catch (ElementNotFoundException e) {
            log.error("Error while updating user", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Update user failed. " + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error updating user", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Update user failed", null));
        }
    }

    // lấy ra customer bằng user id
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/get-customer/{user-id}")
    public ResponseEntity<ObjectResponse> getCustomerByUserID(@PathVariable("user-id") int userID) {
        CustomerDTO customer = userService.getCustomerByUserID(userID);
        return customer != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get customer by user ID successfully", customer)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get customer by user ID failed", null));
    }

//    // lấy ra tất cả user bằng role
//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("/get-users-by-role/{role-id}")
//    public ResponseEntity<ObjectResponse> getUserByRole(@PathVariable("role-id") Integer roleID) {
//        List<User> results = userService.getUsersByRole(roleID);
//        return results != null ?
//                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get user by role name successfully", results)) :
//                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get user by role name failed", null));
//    }

    // lấy tất cả các order activity từ user id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all-order-activities/{user-id}")
    public ResponseEntity<ObjectResponse> getOrderActivitiesByUserID(@PathVariable("user-id") int userID) {
        List<OrderActivity> results = userService.getOrderActivitiesByUserID(userID);
        return results != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all order activities by user ID successfully", results)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get all order activities by user ID failed", null));
    }

    // lấy ra order bằng employee id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-order/{employee-id}")
    public ResponseEntity<ObjectResponse> getOrderByEmployeeID(@PathVariable("employee-id") int employeeID) {
        List<Order> results = userService.getOrdersByEmployeeID(employeeID);
        return results != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get order by employee ID successfully", results)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get order by employee ID failed", null));
    }

    // lấy employee by order id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-employee/{order-id}")
    public ResponseEntity<ObjectResponse> getEmployeeByOrderID(@PathVariable("order-id") int orderID) {
        UserDTO results = userService.getEmployeeByOrderID(orderID);
        return results != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get employee by order ID successfully", results)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get employee by order ID failed", null));
    }

    // lấy user từ order activity id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-user/{order-activity-id}")
    public ResponseEntity<ObjectResponse> getUserByOrderActivityID(@PathVariable("order-activity-id") int orderActivityID) {
        UserDTO results = userService.getUserByOrderActivityID(orderActivityID);
        return results != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get user by order activity ID successfully", results)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get user by order activity ID failed", null));
    }

    // khôi phục lại user đó, bao gồm unlock và undelete
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/undelete/{user-id}")
    public ResponseEntity<ObjectResponse> unDeleteUserByID(@PathVariable("user-id") int userID) {
        try {
            UserDTO user = userService.undeletedUser(userID);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Undelete user successfully", user));
        } catch (ElementNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Undelete user failed. " + e.getMessage(), null));
        } catch (UnchangedStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Undelete user failed. " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Undelete user failed", null));
        }
    }

    // tạo ra user với role
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ObjectResponse> createUserWithRole(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        try {
            UserDTO user = userService.createUserWithRole(userCreateRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Create user successfully", user));
        } catch (Exception e) {
            log.error("Error creating user", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Create user failed", null));
        }
    }

    // set deleted = true and enabled = false
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/lock/{user-id}")
    public ResponseEntity<ObjectResponse> lockUserByID(@PathVariable("user-id") int userID) {
        try {
            User user = userService.findById(userID);
            if(user != null) {
                user.setEnabled(false);
                user.setNonLocked(false);
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Lock user successfully", userService.save(user)));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Lock user failed", null));
        } catch (Exception e) {
            log.error("Error locking user", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Lock user failed", null));
        }
    }

    // xóa user, tức set deleted = true and enabled = false và xóa luôn Customer
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{user-id}")
    public ResponseEntity<ObjectResponse> deleteUserByID(@PathVariable("user-id") int userID) {
        try {
            UserDTO user = userService.deleteUser(userID);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Delete user successfully", user));
        } catch (Exception e) {
            log.error("Error deleting user", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Delete user failed", null));
        }
    }

}
