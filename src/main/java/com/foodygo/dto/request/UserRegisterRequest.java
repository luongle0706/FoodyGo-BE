package com.foodygo.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterRequest {

    @Email(message = "Email không hợp lệ")
    @NotNull(message = "Vui lòng nhập email")
    @NotBlank(message = "Email không được để trống")
    @Size(max = 255, min = 10, message = "Email phải từ 10 tới 255 kí tự bao gồm cả @gmail.com")
    private String email;

    @NotNull(message = "Vui lòng nhập mật khẩu")
    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(max = 100, min = 6, message = "Mật khẩu phải từ 6 tới 100 kí tự")
    private String password;

    @NotNull(message = "Vui lòng nhập tên")
    @NotBlank(message = "Tên không được để trống")
    @Size(max = 255, min = 2, message = "Tên phải từ 2 tới 255 kí tự")
    private String fullName;

    @NotNull(message = "Vui lòng nhập số điện thoại")
    @NotBlank(message = "Số điện thoại không được để trống")
    @Size(max = 12, min = 9, message = "Số điện thoại phải từ 9 tới 12 kí tự")
    private String phone;

    private MultipartFile image;

    @Positive(message = "BuildingID must be positive")
    private Integer buildingID;

}
