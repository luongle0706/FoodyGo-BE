package com.foodygo.service.impl;

import com.foodygo.entity.Role;
import com.foodygo.enums.EnumRoleNameType;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.repository.RoleRepository;
import com.foodygo.service.spec.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByRoleName(EnumRoleNameType roleName) {
        Role role = roleRepository.getRoleByRoleName(roleName);
        if (role == null) {
            throw new ElementNotFoundException("Role not found");
        }
        return role;
    }

    @Override
    public Role getRoleByRoleId(int roleId) {
        Role role = roleRepository.getRolesByRoleID(roleId);
        if (role == null) {
            throw new ElementNotFoundException("Role not found");
        }
        return role;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
