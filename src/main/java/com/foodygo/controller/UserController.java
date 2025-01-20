package com.foodygo.controller;

import com.foodygo.dto.request.UserCreateRequest;
import com.foodygo.dto.request.UserUpdateRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.entity.*;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sdw391/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/test")
    public String testne() {
        return "testne";
    }

    // update user bằng id
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update/{user-id}")
    public ResponseEntity<ObjectResponse> updateHub(@PathVariable("user-id") int userID, @RequestBody UserUpdateRequest userUpdateRequest) {
        try {
            User user = userService.updateUser(userUpdateRequest, userID);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Update user successfully", user));
        } catch (ElementNotFoundException e) {
            log.error("Error while updating user", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Update user failed. User not found", null));
        } catch (Exception e) {
            log.error("Error updating hub", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Update user failed", null));
        }
    }

    // lấy ra customer bằng user id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-customer/{user-id}")
    public ResponseEntity<ObjectResponse> getBuildingByID(@PathVariable("user-id") int userID) {
        Customer customer = userService.getCustomerByUserID(userID);
        return customer != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get customer by user ID successfully", customer)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Get customer by user ID failed", null));
    }

    // lấy tất cả các order activity từ user id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all-order-activity/{user-id}")
    public ResponseEntity<ObjectResponse> getOrdersByHubID(@PathVariable("user-id") int userID) {
        List<OrderActivity> results = userService.getOrderActivitiesByUserID(userID);
        return results != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all order activities by user ID successfully", results)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Get all order activities by user ID failed", null));
    }

    // lấy user từ order activity id
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-user/{order-activity-id}")
    public ResponseEntity<ObjectResponse> getHubByOrderID(@PathVariable("order-activity-id") int orderActivityID) {
        User results = userService.getUserByOrderActivityID(orderActivityID);
        return results != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get user by order activity ID successfully", results)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Get user by order activity ID failed", null));
    }

    // khôi phục lại user đó
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/undelete/{user-id}")
    public ResponseEntity<ObjectResponse> unDeleteHubByID(@PathVariable("user-id") int userID) {
        return userService.undeletedUser(userID) != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Undelete user successfully", userService.findById(userID))) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Undelete user failed", null));
    }

    // tạo ra user với role
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<ObjectResponse> createUserWithRole(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        try {
            User user = userService.createUserWithRole(userCreateRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Create user successfully", user));
        } catch (Exception e) {
            log.error("Error creating hub", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Create user failed", null));
        }
    }

    // xóa user, tức set deleted = true and enabled = false
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{user-id}")
    public ResponseEntity<ObjectResponse> deleteHubByID(@PathVariable("user-id") int userID) {
        try {
            User user = userService.findById(userID);
            if(user != null) {
                user.setDeleted(true);
                user.setEnabled(false);
                user.setNonLocked(false);
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Delete user successfully", userService.save(user)));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Delete user failed", null));
        } catch (Exception e) {
            log.error("Error deleting hub", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Fail", "Delete hub failed", null));
        }
    }

}
