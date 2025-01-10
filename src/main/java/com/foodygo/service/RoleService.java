package com.foodygo.service;


import com.foodygo.enums.EnumRoleName;
import com.foodygo.entity.Role;

import java.util.List;

public interface RoleService {
    Role getRoleByRoleName(EnumRoleName roleName);

    List<Role> getAllRoles();
}
