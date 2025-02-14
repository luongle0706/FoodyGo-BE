package com.foodygo.service;

import com.foodygo.dto.CustomerDTO;
import com.foodygo.dto.UserDTO;
import com.foodygo.dto.request.UserCreateRequest;
import com.foodygo.dto.request.UserRegisterRequest;
import com.foodygo.dto.request.UserUpdateRequest;
import com.foodygo.dto.request.UserUpdateRoleRequest;
import com.foodygo.dto.response.PagingResponse;
import com.foodygo.dto.response.TokenResponse;
import com.foodygo.entity.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface UserService extends BaseService<User, Integer> {

    PagingResponse findAllUsers(Integer currentPage, Integer pageSize);

    List<User> getUsersByRole(Integer roleID);

    PagingResponse getAllUsersActive(Integer currentPage, Integer pageSize);

    UserDTO getUserByEmail(String email);

    boolean lockedUser(int id);

    boolean unLockedUser(int id);

    boolean getUserByPhone(String phone);

    void lockedUserByEmail(String email);

    boolean checkEmailOrPhone(String s);

    UserDTO registerUser(UserRegisterRequest userRegisterRequest);

    UserDTO updateUser(UserUpdateRequest userUpdateRequest, int userID);

    UserDTO updateUserRole(UserUpdateRoleRequest userUpdateRoleRequest, int userID);

    TokenResponse refreshToken(String refreshToken);

    TokenResponse login(String email, String password);

    boolean logout(HttpServletRequest request, HttpServletResponse response);

    TokenResponse getTokenLoginFromOauth2();

    UserDTO createUserWithRole(UserCreateRequest userCreateRequest);

    UserDTO undeletedUser(int userID);

    CustomerDTO getCustomerByUserID(int userID);

    List<OrderActivity> getOrderActivitiesByUserID(int userID);

    UserDTO getUserByOrderActivityID(int orderActivityID);

    List<Order> getOrdersByEmployeeID(int userID);

    UserDTO getEmployeeByOrderID(int orderID);

    UserDTO deleteUser(int userID);

    int countNumberOfRegisterToday();

}
