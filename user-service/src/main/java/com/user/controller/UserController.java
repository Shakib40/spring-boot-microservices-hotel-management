package com.user.controller;

import com.user.dto.ApiResponse;
import com.user.dto.UserRequest;
import com.user.dto.UserResponse;
import com.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("Received request to create user with email: {}", userRequest.getEmail());
        UserResponse response = userService.createUser(userRequest);
        log.info("Successfully created user with id: {}", response.getId());
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .status("SUCCESS")
                .message("User created successfully")
                .response(response)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        log.info("Received request to fetch all users");
        List<UserResponse> users = userService.getAllUsers();
        log.info("Successfully fetched {} users", users.size());
        ApiResponse<List<UserResponse>> apiResponse = ApiResponse.<List<UserResponse>>builder()
                .status("SUCCESS")
                .message("Users fetched successfully")
                .response(users)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable String id) {
        log.info("Received request to fetch user with id: {}", id);
        UserResponse response = userService.getUserById(id);
        log.info("Successfully fetched user with id: {}", id);
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .status("SUCCESS")
                .message("User fetched successfully")
                .response(response)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable String id,
            @Valid @RequestBody UserRequest userRequest) {
        log.info("Received request to update user with id: {}", id);
        UserResponse response = userService.updateUserDetails(id, userRequest);
        log.info("Successfully updated user with id: {}", id);
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .status("SUCCESS")
                .message("User details updated successfully")
                .response(response)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/reset-password/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserPassword(@PathVariable String id,
            @Valid @RequestBody String newPassword) {
        log.info("Received request to update user password with id: {}", id);
        UserResponse response = userService.updateUserPassword(id, newPassword);
        log.info("Successfully updated user password with id: {}", id);
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .status("SUCCESS")
                .message("successfully reset password")
                .response(response)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id) {
        log.info("Received request to delete user with id: {}", id);
        userService.deleteUser(id);
        log.info("Successfully deleted user with id: {}", id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .status("SUCCESS")
                .message("User deleted successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
