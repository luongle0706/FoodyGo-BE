package com.foodygo.controller;

import com.foodygo.dto.UserDTO;
import com.foodygo.dto.request.UserLoginRequest;
import com.foodygo.dto.request.UserRegisterRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.dto.response.TokenResponse;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.service.spec.FirebaseService;
import com.foodygo.service.spec.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/authentications")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication")
public class MainController {

    private final UserService userService;
    private final FirebaseService firebaseService;

    @PostMapping("/firebase")
    public ResponseEntity<ObjectResponse> loginUsingFirebase(
            @RequestParam String googleIdToken,
            @RequestParam String fcmToken
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ObjectResponse.builder()
                                .status(HttpStatus.OK.toString())
                                .message("Successful login using firebase")
                                .data(firebaseService.getUserFromFirebase(googleIdToken, fcmToken))
                                .build()
                );
    }

    /**
     * Method Register account
     *
     * @param userRegisterRequest userRegisterRequest
     * @return user or null
     */
    @Operation(summary = "Register account", description = "User register an account to login")
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ObjectResponse> userRegister(
            @RequestPart("userRegisterRequest") UserRegisterRequest userRegisterRequest,
            @RequestPart("image") MultipartFile image) {
        try {
            userRegisterRequest.setImageCustomer(image);
            UserDTO user = userService.registerUser(userRegisterRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Register user successfully", user));
        } catch (Exception e) {
            log.error("Error register user", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Register user failed", null));
        }
    }

    /**
     * Method Refresh token
     *
     * @param request request
     * @return token or null
     */
    @Operation(summary = "Refresh token", description = "Generate a new token when token exp")
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("RefreshToken");
        TokenResponse tokenResponse = userService.refreshToken(refreshToken);
        return ResponseEntity.status(tokenResponse.getCode().equals("Success") ? HttpStatus.OK : HttpStatus.UNAUTHORIZED).body(tokenResponse);
    }

    /**
     * Method login
     *
     * @param userLogin userLogin
     * @return token, refreshToken or null
     */
    @Operation(summary = "Login", description = "Login")
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginPage(@Valid @RequestBody UserLoginRequest userLogin, @RequestParam(required = false) String fcmToken) {
        try {
            TokenResponse tokenResponse = fcmToken != null ? userService.login(userLogin.getEmail(), userLogin.getPassword(), fcmToken) : userService.login(userLogin.getEmail(), userLogin.getPassword());
            return ResponseEntity.status(tokenResponse.getCode().equals("Success") ? HttpStatus.OK : HttpStatus.UNAUTHORIZED).body(tokenResponse);
        } catch (Exception e) {
            log.error("Cannot login : {}", e.toString());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    TokenResponse.builder()
                            .code("Failed")
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @PostMapping("/opt-out")
    public ResponseEntity<ObjectResponse> logout(
            @RequestParam String fcmToken
    ) {
        userService.invalidateFcmToken(fcmToken);
        return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "FCM Token invalidated", null));
    }

    /**
     * Method logout
     *
     * @param request  request
     * @param response response
     * @return success or failed
     */
    @Operation(summary = "Logout", description = "Logout")
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

    /**
     * Method get Token from login Oauth2
     *
     * @return token, refreshToken or null
     */
    @Operation(summary = "Logout", description = "Logout")
    @GetMapping("/oauth2-token")
    public ResponseEntity<TokenResponse> getToken() {
        try {
            TokenResponse tokenResponse = userService.getTokenLoginFromOauth2();
            return ResponseEntity.status(tokenResponse.getCode().equals("Success") ? HttpStatus.OK : HttpStatus.UNAUTHORIZED).body(tokenResponse);
        } catch (Exception e) {
            log.error("Cannot login : {}", e.toString());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    TokenResponse.builder()
                            .code("Failed")
                            .message("Login failed")
                            .build()
            );
        }
    }
}
