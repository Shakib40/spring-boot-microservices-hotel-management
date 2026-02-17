package com.user.service;

import com.user.dto.UserRequest;
import com.user.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);

    UserResponse getUserById(String id);

    List<UserResponse> getAllUsers();

    UserResponse updateUserDetails(String id, UserRequest userRequest);

    UserResponse updateUserPassword(String id, String newPassword);

    void deleteUser(String id);
}
