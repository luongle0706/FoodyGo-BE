package com.foodygo.controller;

import com.foodygo.dto.UserDTO;
import com.foodygo.dto.request.OTPRequest;
import com.foodygo.dto.response.OTPResponse;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.service.impl.EmailService;
import com.foodygo.service.spec.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Hub", description = "Operations related to hub management")
public class EmailController {

    private final EmailService emailService;
    private final UserService userService;

    @PostMapping("/send-otp")
    public ResponseEntity<ObjectResponse> verifyOtp(@RequestBody OTPRequest request) {
        OTPResponse otpResponse = new OTPResponse();
            otpResponse.setExistedEmail(userService.existEmailUser(request.getEmail()));
        String storedOtp = emailService.sendOTP(request.getEmail());
        if(storedOtp != null) {
            otpResponse.setOtp(storedOtp);
        }
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(OK.toString())
                                .message("Send email success")
                                .data(otpResponse)
                                .build()
                );    }

}
