package com.foodygo.service;

import com.foodygo.dto.request.UserCreateRequest;
import com.foodygo.dto.request.UserRegisterRequest;
import com.foodygo.dto.request.UserUpdateRequest;
import com.foodygo.entity.*;
import com.foodygo.enums.EnumRoleName;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.repository.UserRepository;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

@Service
@Slf4j
public class UserServiceImp extends BaseServiceImp<User, Integer> implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImp(UserRepository userRepository, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        super(userRepository);
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public List<User> getUsersByRole(String role) {
        List<User> listsByRole = userRepository.findAll();
        Role role_admin = roleService.getRoleByRoleName(EnumRoleName.ROLE_ADMIN);
        Role role_manager = roleService.getRoleByRoleName(EnumRoleName.ROLE_MANAGER);

        if (role.equals(EnumRoleName.ROLE_STAFF.name())) {
            for (User user : userRepository.findAll()) {
                if (user.getRole().equals(role_admin) || user.getRole().equals(role_manager)) {
                    listsByRole.remove(user);
                }
            }
        }  else if (role.equals(EnumRoleName.ROLE_ADMIN.name())) {
                return userRepository.findAll();
        }
        return listsByRole;
    }

    @Override
    public boolean lockedUser(int id) {
        User user = userRepository.getUserByUserID(id);
        if (user != null && user.isNonLocked()) {
            userRepository.locked(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean unLockedUser(int id) {
        User user = userRepository.getUserByUserID(id);
        if (user != null && !user.isNonLocked()) {
            userRepository.unLocked(id);
            return true;
        }
        return false;
    }


    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public boolean getUserByPhone(String phone) {
        User user = userRepository.getUserByPhone(phone);
        if (user == null || !user.isNonLocked() || !user.isEnabled()) {
            return false;
        }
        return true;
    }

    @Override
    public void lockedUserByEmail(String email) {
        User user = userRepository.getUserByEmail(email);
        if (user != null && user.isNonLocked()) {
            userRepository.lockedByEmail(email);
        }
    }

    @Override
    public boolean checkEmailOrPhone(String s) {
        User user = null;
        boolean check = false;
        char c = s.toLowerCase().charAt(0);
        if (c >= 'a' && c <= 'z') {
            user = userRepository.getUserByEmail(s);
            check = true;
        } else if (c >= '0' && c <= '9') {
            user = userRepository.getUserByPhone(s);
            check = false;
        }
        return check;
    }

    @Override
    public User createUser(UserRegisterRequest userRegisterRequest) {
        Role role = roleService.getRoleByRoleName(EnumRoleName.ROLE_USER);
        User user = User.builder()
                .email(userRegisterRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(userRegisterRequest.getPassword()))
                .accessToken(null)
                .refreshToken(null)
                .enabled(true)
                .nonLocked(true)
                .role(role)
                .build();
        return userRepository.save(user);
    }

    @Override
    public User updateUser(UserUpdateRequest userUpdateRequest, int userID) {
        User user = userRepository.getUserByUserID(userID);
        if (user != null) {
            if (userUpdateRequest.getPassword() != null) {
                user.setPassword(bCryptPasswordEncoder.encode(userUpdateRequest.getPassword()));
            }
            if (userUpdateRequest.getPhone() != null) {
                user.setPhone(userUpdateRequest.getPhone());
            }
            if(userUpdateRequest.getFullName() != null) {
                user.setFullName(userUpdateRequest.getFullName());
            }
            return userRepository.save(user);
        } else {
            throw new ElementNotFoundException("User not found");
        }
    }

    @Override
    public User createUserWithRole(UserCreateRequest userCreateRequest) {
        Role role = roleService.getRoleByRoleId(userCreateRequest.getRoleID());
        User user = User.builder()
                .email(userCreateRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(userCreateRequest.getPassword()))
                .accessToken(null)
                .refreshToken(null)
                .enabled(true)
                .nonLocked(true)
                .role(role)
                .build();
        return userRepository.save(user);
    }

    @Override
    public User undeletedUser(int userID) {
        User user = userRepository.getUserByUserID(userID);
        if (user != null && !user.isNonLocked() && !user.isEnabled() && user.isDeleted()) {
            user.setNonLocked(true);
            user.setDeleted(false);
            user.setEnabled(true);
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public Customer getCustomerByUserID(int userID) {
        User user = userRepository.getUserByUserID(userID);
        if (user != null) {
            return user.getCustomer();
        }
        return null;
    }

    @Override
    public List<OrderActivity> getOrderActivitiesByUserID(int userID) {
        // chua co order activity
        return List.of();
    }

    @Override
    public User getUserByOrderActivityID(int orderActivityID) {
        // chua co order activity
        return null;
    }

    @Override
    public List<Order> getOrdersByEmployeeID(int userID) {
        User user = userRepository.getUserByUserID(userID);
        if (user == null) {
            throw new ElementNotFoundException("User not found");
        }
        return user.getEmployeeOrders();
    }

    @Override
    public User getEmployeeByOrderID(int orderID) {
        // chua co order service
        return null;
    }

}
