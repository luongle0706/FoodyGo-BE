package com.foodygo.controllers;

import com.foodygo.configurations.CustomUserDetail;
import com.foodygo.configurations.JWTAuthenticationFilter;
import com.foodygo.configurations.JWTToken;
import com.foodygo.dtos.requests.UserLoginRequest;
import com.foodygo.dtos.responses.ObjectResponse;
import com.foodygo.dtos.responses.TokenResponse;
import com.foodygo.enums.EnumTokenType;
import com.foodygo.pojos.User;
import com.foodygo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
public class MainController {

    @Autowired private UserService userService;
    @Autowired private JWTToken jwtToken;
    @Autowired private JWTAuthenticationFilter jwtAuthenticationFilter;
    @Autowired private AuthenticationManager authenticationManager;

    @GetMapping("/test")
    public String testne() {
        return "testne2";
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<TokenResponse> refreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("RefreshToken");
        String email = jwtToken.getEmailFromJwt(refreshToken, EnumTokenType.REFRESH_TOKEN);
        User user = userService.getUserByEmail(email);
        if(user != null) {
            if(StringUtils.hasText(refreshToken) && user.getRefreshToken().equals(refreshToken)) {
                if(jwtToken.validate(refreshToken, EnumTokenType.REFRESH_TOKEN)) {
                    CustomUserDetail customUserDetail = CustomUserDetail.mapUserToUserDetail(user);
                    if(customUserDetail != null) {
                        String newToken = jwtToken.generatedToken(customUserDetail);
                        return ResponseEntity.status(HttpStatus.OK).body(new TokenResponse("Success", "Refresh token successfully", newToken, refreshToken));
                    }
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse("Failed", "Refresh token failed", null, null));
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse("Failed", "Refresh token failed", null, null));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse("Failed", "Refresh token failed", null, null));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginPage(@Valid @RequestBody UserLoginRequest userLogin) {
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword());
            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String token = jwtToken.generatedToken(userDetails);
            String refreshToken = jwtToken.generatedRefreshToken(userDetails);

            return ResponseEntity.status(HttpStatus.OK).body(new TokenResponse("Success", "Login successfully", token, refreshToken));
        } catch(Exception e) {
            log.error("Cannot login : {}", e.toString());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse("Failed", "Login failed", null, null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ObjectResponse> getLogout(HttpServletRequest request) {
        try {
            String token = jwtAuthenticationFilter.getToken(request);
            String email = jwtToken.getEmailFromJwt(token, EnumTokenType.TOKEN);
            User user = userService.getUserByEmail(email);
            user.setAccessToken(null);
            user.setRefreshToken(null);
        } catch (Exception e) {
            log.error("Error logout : {}", e.toString());
        } finally {
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Logout successfully", null));
        }
    }

}
