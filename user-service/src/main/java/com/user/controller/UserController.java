package com.user.controller;

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
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("Received request to create user with email: {}", userRequest.getEmail());
        UserResponse response = userService.createUser(userRequest);
        log.info("Successfully created user with id: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("Received request to fetch all users");
        List<UserResponse> users = userService.getAllUsers();
        log.info("Successfully fetched {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        log.info("Received request to fetch user with id: {}", id);
        UserResponse response = userService.getUserById(id);
        log.info("Successfully fetched user with id: {}", id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String id,
            @Valid @RequestBody UserRequest userRequest) {
        log.info("Received request to update user with id: {}", id);
        UserResponse response = userService.updateUser(id, userRequest);
        log.info("Successfully updated user with id: {}", id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        log.info("Received request to delete user with id: {}", id);
        userService.deleteUser(id);
        log.info("Successfully deleted user with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
