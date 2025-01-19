package com.foodygo.controller;

import com.foodygo.dto.request.HubUpdateRequest;
import com.foodygo.dto.request.UserUpdateRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.entity.Hub;
import com.foodygo.entity.User;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

}
