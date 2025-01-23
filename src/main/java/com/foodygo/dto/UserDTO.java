package com.foodygo.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    int userID;
    String phone;
    String fullName;
    String email;
    String roleName;
    boolean enabled;
    boolean nonLocked;
    boolean deleted;
}
