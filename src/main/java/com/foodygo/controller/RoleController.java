package com.foodygo.controller;

import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.entity.Role;
import com.foodygo.service.spec.RoleService;
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
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "User", description = "Operations related to user management")
public class RoleController {

    private final RoleService roleService;

    /**
     * Method get all roles
     *
     * @return list or empty
     */
    @Operation(summary = "Get all roles", description = "Retrieves all roles")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<ObjectResponse> getAllRoles() {
        List<Role> results = roleService.getAllRoles();
        return !results.isEmpty() ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all roles successfully", results)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get all roles failed", null));
    }

}
