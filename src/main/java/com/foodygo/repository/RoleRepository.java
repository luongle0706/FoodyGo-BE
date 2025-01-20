package com.foodygo.repository;

import com.foodygo.enums.EnumRoleName;
import com.foodygo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role getRoleByRoleName(EnumRoleName role);

    Role getRolesByRoleID(int roleID);

}
