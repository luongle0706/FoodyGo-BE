package com.foodygo.repositories;

import com.foodygo.enums.EnumRoleName;
import com.foodygo.pojos.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    public Role getRoleByRoleName(EnumRoleName role);
}
