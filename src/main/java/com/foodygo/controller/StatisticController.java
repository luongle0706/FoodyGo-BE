package com.foodygo.controller;

import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/statistic")
@RequiredArgsConstructor
@Tag(name = "Statistic", description = "Operations related to statistic")
public class StatisticController {

    private final UserService userService;

    /**
     * Method count number of user register today
     *
     * @return list or empty
     */
    @Operation(summary = "Count number of user register today", description = "Count number of user register today")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/count-number-register-today")
    public ResponseEntity<ObjectResponse> countNumberOfRegisterToday() {
        int results = userService.countNumberOfRegisterToday();
        return results > 0 ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all roles successfully", results)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get all roles failed", 0));
    }

}
