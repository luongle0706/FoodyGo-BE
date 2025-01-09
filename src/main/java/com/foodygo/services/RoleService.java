package com.foodygo.services;


import com.foodygo.enums.EnumRoleName;
import com.foodygo.pojos.Role;

import java.util.List;

public interface RoleService {
    public Role getRoleByRoleName(EnumRoleName roleName);

    public List<Role> getAllRoles();
}
