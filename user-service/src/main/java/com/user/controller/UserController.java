package com.user.controller;

import com.user.dto.ApiResponse;
import com.user.dto.UserRequest;
import com.user.dto.UserResponse;
import com.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        public ResponseEntity<Page<UserResponse>> getAllUsers(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(required = false) String role,
                        @RequestParam(defaultValue = "desc") String sortDir) {

                Pageable pageable = PageRequest.of(
                                page,
                                size,
                                sortDir.equalsIgnoreCase("asc") ? Sort.by("createdDate").ascending()
                                                : Sort.by("createdDate").descending());

                Page<UserResponse> users = userService.getAllUsers(role, pageable);

                return ResponseEntity.ok(users);
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable String id) {
                UserResponse response = userService.getUserById(id);
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
                UserResponse response = userService.updateUserDetails(id, userRequest);
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
                UserResponse response = userService.updateUserPassword(id, newPassword);
                ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                                .status("SUCCESS")
                                .message("successfully reset password")
                                .response(response)
                                .build();
                return ResponseEntity.ok(apiResponse);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id) {
                userService.deleteUser(id);
                ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                                .status("SUCCESS")
                                .message("User deleted successfully")
                                .build();
                return ResponseEntity.ok(apiResponse);
        }
}
