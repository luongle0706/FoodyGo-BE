package com.foodygo.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    @Size(max = 10, min = 10, message = "Phone phải có 10 chữ số")
    private String phone;

    @Size(max = 255, min = 1, message = "FullName phải từ 1 tới 255 kí tự")
    private String fullName;

    @Size(max = 100, min = 6, message = "Mật khẩu phải từ 6 tới 100 kí tự")
    private String password;

}
