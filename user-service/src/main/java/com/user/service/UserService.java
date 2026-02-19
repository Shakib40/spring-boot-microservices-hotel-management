package com.user.service;

import com.user.dto.UserRequest;
import com.user.dto.UserResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);

    UserResponse getUserById(String id);

    Page<UserResponse> getAllUsers(String role, Pageable pageable);

    UserResponse updateUserDetails(String id, UserRequest userRequest);

    UserResponse updateUserPassword(String id, String newPassword);

    void deleteUser(String id);
}
