package com.foodygo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {

    @Email(message = "Invalid email")
    @NotNull(message = "Please enter email")
    @NotBlank(message = "Email is not blank")
    @Size(max = 255, min = 10, message = "Email must be between 10 and 255 characters including @gmail.com")
    private String email;

    @NotNull(message = "Please enter password")
    @NotBlank(message = "Password is not blank")
    @Size(max = 100, min = 6, message = "Password must be between 6 and 100 characters")
    private String password;

}
