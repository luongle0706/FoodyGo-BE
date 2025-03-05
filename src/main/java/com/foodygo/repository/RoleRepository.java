package com.foodygo.repository;

import com.foodygo.enums.EnumRoleNameType;
import com.foodygo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role getRoleByRoleName(EnumRoleNameType role);

    Role getRolesByRoleID(int roleID);

}
