package com.foodygo.service;

import com.foodygo.configuration.CustomUserDetail;
import com.foodygo.dto.CustomerDTO;
import com.foodygo.dto.UserDTO;
import com.foodygo.dto.request.UserCreateRequest;
import com.foodygo.dto.request.UserRegisterRequest;
import com.foodygo.dto.request.UserUpdateRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.dto.response.PagingResponse;
import com.foodygo.entity.*;
import com.foodygo.enums.EnumRoleNameType;
import com.foodygo.exception.AuthenticationException;
import com.foodygo.exception.ElementExistException;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.exception.UnchangedStateException;
import com.foodygo.mapper.CustomerMapper;
import com.foodygo.mapper.UserMapper;
import com.foodygo.repository.CustomerRepository;
import com.foodygo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl extends BaseServiceImpl<User, Integer> implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;
    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder, UserMapper userMapper, CustomerMapper customerMapper, CustomerRepository customerRepository) {
        super(userRepository);
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper = userMapper;
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;
    }

    @Override
    public PagingResponse findAllUsers(Integer currentPage, Integer pageSize) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

        var pageData = userRepository.findAll(pageable);

        return PagingResponse.builder()
                .currentPage(currentPage)
                .pageSizes(pageSize)
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .data(pageData.getContent().stream()
                        .map(userMapper::userToUserDTO)
                        .toList())
                .build();
    }

//    private String getRoleByRoleID(Integer roleID) {
//        if (roleID == null) {
//            throw new ElementNotFoundException("Role ID is null");
//        }
//        return switch (roleID) {
//            case 1 -> "ROLE_ADMIN";
//            case 2 -> "ROLE_STAFF";
//            case 3 -> "ROLE_USER";
//            case 4 -> "ROLE_MANAGER";
//            case 5 -> "ROLE_SELLER";
//            default -> throw new ElementNotFoundException("Role ID is not valid");
//        };
//    }

//    @Override
//    public List<User> getUsersByRole(Integer roleID) {
//
//        String role = getRoleByRoleID(roleID);
//
//        List<User> listsByRole = userRepository.findAll();
//        Role role_admin = roleService.getRoleByRoleName(EnumRoleNameType.ROLE_ADMIN);
//        Role role_manager = roleService.getRoleByRoleName(EnumRoleNameType.ROLE_MANAGER);
//        Role role_staff = roleService.getRoleByRoleName(EnumRoleNameType.ROLE_STAFF);
//
//        if (role.equals(EnumRoleNameType.ROLE_STAFF.name())) {
//            for (User user : userRepository.findAll()) {
//                if (user.getRole().equals(role_admin) || user.getRole().equals(role_manager)) {
//                    listsByRole.remove(user);
//                }
//            }
//        }  else if (role.equals(EnumRoleNameType.ROLE_ADMIN.name())) {
//                return userRepository.findAll();
//        }
//        return listsByRole;
//    }

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
    public UserDTO registerUser(UserRegisterRequest userRegisterRequest) {
        User checkExistingUser = userRepository.getUserByEmail(userRegisterRequest.getEmail());
        if (checkExistingUser != null) {
            throw new ElementExistException("User already exists");
        }
        Role role = roleService.getRoleByRoleName(EnumRoleNameType.ROLE_USER);
        User user = User.builder()
                .email(userRegisterRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(userRegisterRequest.getPassword()))
                .accessToken(null)
                .refreshToken(null)
                .enabled(true)
                .nonLocked(true)
                .role(role)
                .build();
        return userMapper.userToUserDTO(userRepository.save(user));
    }

    @Override
    public UserDTO updateUser(UserUpdateRequest userUpdateRequest, int userID) {

        CustomUserDetail customUserDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (customUserDetail.getUserID() != userID) {
            throw new AuthenticationException("You are not allowed to update other user");
        }

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
            return userMapper.userToUserDTO(userRepository.save(user));
        } else {
            throw new ElementNotFoundException("User not found");
        }
    }

    @Override
    public UserDTO createUserWithRole(UserCreateRequest userCreateRequest) {
        User checkExistingUser = userRepository.getUserByEmail(userCreateRequest.getEmail());
        if (checkExistingUser != null) {
            throw new ElementExistException("User already exists");
        }
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
        return userMapper.userToUserDTO(userRepository.save(user));
    }

    @Override
    public UserDTO undeletedUser(int userID) {
        User user = userRepository.getUserByUserID(userID);
        if (user == null) {
            throw new ElementNotFoundException("User not found");
        }
        if(user.isNonLocked() && user.isEnabled() && !user.isDeleted()) {
            throw new UnchangedStateException("User already deleted");
        }
        user.setNonLocked(true);
        user.setDeleted(false);
        user.setEnabled(true);
        return userMapper.userToUserDTO(userRepository.save(user));
    }

    @Override
    public CustomerDTO getCustomerByUserID(int userID) {
        User user = userRepository.getUserByUserID(userID);
        if (user != null) {
            return customerMapper.customerToCustomerDTO(user.getCustomer());
        }
        return null;
    }

    @Override
    public List<OrderActivity> getOrderActivitiesByUserID(int userID) {
        // chua co order activity
        return List.of();
    }

    @Override
    public UserDTO getUserByOrderActivityID(int orderActivityID) {
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
    public UserDTO getEmployeeByOrderID(int orderID) {
        // chua co order service
        return null;
    }

    @Override
    public UserDTO deleteUser(int userID) {
        User user = userRepository.getUserByUserID(userID);
        Customer customer = user.getCustomer();
        if(user == null) {
            throw new ElementNotFoundException("User not found");
        }
        user.setDeleted(true);
        user.setEnabled(false);
        user.setNonLocked(false);
        if (customer != null) {
            customer.setDeleted(true);
            customerRepository.save(customer);
        }
        return userMapper.userToUserDTO(userRepository.save(user));
    }

}
