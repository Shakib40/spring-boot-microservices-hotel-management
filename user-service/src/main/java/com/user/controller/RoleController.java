package com.user.controller;

import com.user.dto.RoleRequest;
import com.user.dto.RoleResponse;
import com.user.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Slf4j
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleRequest roleRequest) {
        log.info("Received request to create role: {}", roleRequest.getName());
        RoleResponse response = roleService.createRole(roleRequest);
        log.info("Successfully created role with id: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        log.info("Received request to fetch all roles");
        List<RoleResponse> roles = roleService.getAllRoles();
        log.info("Successfully fetched {} roles", roles.size());
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable String id) {
        log.info("Received request to fetch role with id: {}", id);
        RoleResponse response = roleService.getRoleById(id);
        log.info("Successfully fetched role with id: {}", id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<RoleResponse> getRoleByName(@PathVariable String name) {
        log.info("Received request to fetch role with name: {}", name);
        RoleResponse response = roleService.getRoleByName(name);
        log.info("Successfully fetched role with name: {}", name);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> updateRole(@PathVariable String id,
            @Valid @RequestBody RoleRequest roleRequest) {
        log.info("Received request to update role with id: {}", id);
        RoleResponse response = roleService.updateRole(id, roleRequest);
        log.info("Successfully updated role with id: {}", id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable String id) {
        log.info("Received request to delete role with id: {}", id);
        roleService.deleteRole(id);
        log.info("Successfully deleted role with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
