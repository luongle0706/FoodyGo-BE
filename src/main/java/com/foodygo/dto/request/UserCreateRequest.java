package com.foodygo.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {

    @Email(message = "Email không hợp lệ")
    @NotNull(message = "Vui lòng nhập email")
    @NotBlank(message = "Email không được để trống")
    @Size(max = 255, min = 10, message = "Email phải từ 10 tới 255 kí tự bao gồm cả @gmail.com")
    private String email;

    @NotNull(message = "Vui lòng nhập mật khẩu")
    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(max = 100, min = 6, message = "Mật khẩu phải từ 6 tới 100 kí tự")
    private String password;

    @Positive(message = "RoleID must be positive")
    @Max(value = 4, message = "RoleID must be between 1 and 4")
    private int roleID;

}
