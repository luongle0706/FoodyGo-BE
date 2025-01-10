package com.foodygo.service;

import com.foodygo.enums.EnumRoleName;
import com.foodygo.entity.Role;
import com.foodygo.entity.User;
import com.foodygo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Override
    public List<User> findAll(String role) {
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
    public void save(User user) {
        userRepository.save(user);
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
    public User getUserByID(int userID) {
        return userRepository.getUserByUserID(userID);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public boolean verifyAccount(String code) {
        User user = userRepository.getUserByCodeVerify(code);
        if (user == null || user.isEnabled() || !user.isNonLocked()) {
            return false;
        }
        userRepository.enabled(user.getUserID());
        return true;
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

}
