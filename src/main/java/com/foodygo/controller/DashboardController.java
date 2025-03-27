package com.foodygo.controller;

import com.foodygo.dto.response.DashboardResponse;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.service.spec.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.HttpStatus.OK;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/dashboard")
@Tag(name = "Dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping()
    @Operation(summary = "Get Dashboard Data", description = "Retrieve all dashboard statistics and metrics including revenue, orders, and top restaurants.")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dashboard data retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> getDashboardData() {
        DashboardResponse dashboardData = dashboardService.getDashboardData();
        
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(OK.toString())
                                .message("Dashboard data retrieved successfully")
                                .data(dashboardData)
                                .build()
                );
    }
}