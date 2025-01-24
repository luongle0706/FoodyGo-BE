package com.foodygo.mapper;

import com.foodygo.dto.UserDTO;
import com.foodygo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "role.roleName", target = "roleName")
    @Mapping(source = "enabled", target = "enabled")
    @Mapping(source = "nonLocked", target = "nonLocked")
    @Mapping(source = "deleted", target = "deleted")
    @Mapping(source = "userID", target = "userID")
    UserDTO userToUserDTO(User user);
}
