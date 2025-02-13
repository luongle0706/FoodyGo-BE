package com.foodygo.controller;

import com.foodygo.dto.UserDTO;
import com.foodygo.dto.request.UserLoginRequest;
import com.foodygo.dto.request.UserRegisterRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.dto.response.TokenResponse;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public")
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;

    @GetMapping("/test")
    public String testne() {
        return "testne2";
    }

    // user đăng kí tài khoản
    @PostMapping("/register")
    public ResponseEntity<ObjectResponse> userRegister(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        try {
            UserDTO user = userService.registerUser(userRegisterRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Register user successfully", user));
        } catch (Exception e) {
            log.error("Error register user", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Register user failed", null));
        }
    }

    // token refresh
    @PostMapping("/refresh_token")
    public ResponseEntity<TokenResponse> refreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("RefreshToken");
        TokenResponse tokenResponse = userService.refreshToken(refreshToken);
        return ResponseEntity.status(tokenResponse.getCode().equals("Success") ? HttpStatus.OK : HttpStatus.UNAUTHORIZED).body(tokenResponse);
    }

    // login
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginPage(@Valid @RequestBody UserLoginRequest userLogin) {
        try {
            TokenResponse tokenResponse = userService.login(userLogin.getEmail(), userLogin.getPassword());
            return ResponseEntity.status(tokenResponse.getCode().equals("Success") ? HttpStatus.OK : HttpStatus.UNAUTHORIZED).body(tokenResponse);
        } catch (Exception e) {
            log.error("Cannot login : {}", e.toString());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse("Failed", "Login failed", null, null));
        }
    }

    // logout
    @PostMapping("/logout")
    public ResponseEntity<ObjectResponse> getLogout(HttpServletRequest request, HttpServletResponse response) {
        try {
            boolean checkLogout = userService.logout(request, response);
            return checkLogout ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Logout successfully", null)) :
                                 ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Logout failed", null));
        } catch (ElementNotFoundException e) {
            log.error("Error logout not found : {}", e.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Logout failed", null));
        } catch (Exception e) {
            log.error("Error logout : {}", e.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Logout failed", null));
        }
    }

    // get Token from login Oauth2
    @GetMapping("/oauth2-token")
    public ResponseEntity<TokenResponse> getToken() {
        try {
            TokenResponse tokenResponse = userService.getTokenLoginFromOauth2();
            return ResponseEntity.status(tokenResponse.getCode().equals("Success") ? HttpStatus.OK : HttpStatus.UNAUTHORIZED).body(tokenResponse);
        } catch (Exception e) {
            log.error("Cannot login : {}", e.toString());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse("Failed", "Login failed", null, null));
        }
    }

}
