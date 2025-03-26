package com.foodygo.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    int userID;
    String phone;
    String fullName;
    String email;
    String roleName;
    String buildingName;
    String hubName;
    LocalDate dob;
    String image;
    Integer buildingID;
    boolean enabled;
    boolean nonLocked;
    boolean deleted;
}
