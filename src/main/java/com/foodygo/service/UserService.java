package com.foodygo.service;

import com.foodygo.dto.request.UserCreateRequest;
import com.foodygo.dto.request.UserRegisterRequest;
import com.foodygo.dto.request.UserUpdateRequest;
import com.foodygo.entity.*;

import java.util.List;

public interface UserService extends BaseService<User, Integer> {

    List<User> getUsersByRole(String role);

    User getUserByEmail(String email);

    boolean lockedUser(int id);

    boolean unLockedUser(int id);

    boolean getUserByPhone(String phone);

    void lockedUserByEmail(String email);

    boolean checkEmailOrPhone(String s);

    User createUser(UserRegisterRequest userRegisterRequest);

    User updateUser(UserUpdateRequest userUpdateRequest, int userID);

    User createUserWithRole(UserCreateRequest userCreateRequest);

    User undeletedUser(int userID);

    Customer getCustomerByUserID(int userID);

    List<OrderActivity> getOrderActivitiesByUserID(int userID);

    User getUserByOrderActivityID(int orderActivityID);

    List<Order> getOrdersByEmployeeID(int userID);

    User getEmployeeByOrderID(int orderID);
}
