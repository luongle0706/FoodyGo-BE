package com.foodygo.service;

import com.foodygo.entity.Role;
import com.foodygo.enums.EnumRoleName;
import com.foodygo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImp implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByRoleName(EnumRoleName roleName) {
        return roleRepository.getRoleByRoleName(roleName);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
