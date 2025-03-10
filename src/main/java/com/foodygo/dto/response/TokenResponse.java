package com.foodygo.dto.response;

import com.foodygo.enums.EnumRoleNameType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenResponse {
    String code;
    String message;
    String token;
    String refreshToken;
    String fullName;
    String email;
    Integer userId;
    Integer customerId;
    Integer restaurantId;
    EnumRoleNameType role;
}
