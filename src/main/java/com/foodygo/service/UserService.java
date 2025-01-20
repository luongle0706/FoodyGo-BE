package com.foodygo.service;

import com.foodygo.dto.request.UserRegisterRequest;
import com.foodygo.dto.request.UserUpdateRequest;
import com.foodygo.entity.User;
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

}
