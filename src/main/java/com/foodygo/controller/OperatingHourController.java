package com.foodygo.controller;

import com.foodygo.dto.request.OperatingHourUpdateRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.service.spec.OperatingHourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/operating-hours")
@Tag(name = "OperatingHour")
public class OperatingHourController {
    private final OperatingHourService operatingHourService;

    @GetMapping("/{restaurantId}")
    @Operation(summary = "Get Operating Hours of a Restaurant", description = "Get operating hours of a restaurant.")
    @PreAuthorize("hasAnyRole('STAFF', 'SELLER', 'MANAGER', 'ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operating hour found"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Operating Hour not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> getRestaurantOperatingHours(
            @PathVariable Integer restaurantId
    ) {
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(OK.toString())
                                .message("Operating hour found!")
                                .data(operatingHourService.getOperatingHourDTOsByRestaurantId(restaurantId))
                                .build()
                );
    }

    @PutMapping()
    @Operation(summary = "Update Operating Hours of a Restaurant", description = "Update existing operating hours of a restaurant with the provided data.")
    @PreAuthorize("hasAnyRole('STAFF', 'SELLER', 'MANAGER', 'ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Update successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "400", description = "Operating Hour not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectResponse> updateRestaurantOperatingHours(
            @RequestBody OperatingHourUpdateRequest operatingHourUpdateRequest
    ) {
        operatingHourService.updateOperatingHoursByRestaurantId(operatingHourUpdateRequest.getOperatingHourList());
        return ResponseEntity
                .status(OK)
                .body(
                        ObjectResponse.builder()
                                .status(OK.toString())
                                .message("Update successfully!")
                                .build()
                );
    }
}
