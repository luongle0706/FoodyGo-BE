package com.foodygo.service;

import com.foodygo.entity.Role;
import com.foodygo.enums.EnumRoleNameType;
import com.foodygo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByRoleName(EnumRoleNameType roleName) {
        return roleRepository.getRoleByRoleName(roleName);
    }

    @Override
    public Role getRoleByRoleId(int roleId) {
        Role role = roleRepository.getRolesByRoleID(roleId);
        return role != null ? role : null;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
