package com.foodygo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRoleRequest {
    @Size(max = 10, min = 10, message = "Phone phải có 10 chữ số")
    private String phone;

    @Size(max = 255, min = 1, message = "FullName phải từ 1 tới 255 kí tự")
    private String fullName;

    @Min(value = 1, message = "RoleID must be positive")
    private int roleID;
}
