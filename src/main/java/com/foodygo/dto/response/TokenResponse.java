package com.foodygo.dto.response;

import com.foodygo.enums.EnumRoleNameType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
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
    Integer hubId;
    Integer walletId;
    EnumRoleNameType role;
    @Builder.Default
    boolean created = false;
}
