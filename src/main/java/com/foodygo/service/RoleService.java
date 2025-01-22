package com.foodygo.service;


import com.foodygo.enums.EnumRoleNameType;
import com.foodygo.entity.Role;

import java.util.List;

public interface RoleService {
    Role getRoleByRoleName(EnumRoleNameType roleName);

    Role getRoleByRoleId(int roleId);

    List<Role> getAllRoles();
}
