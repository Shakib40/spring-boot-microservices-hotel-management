package com.user.service;

import com.user.dto.UserRequest;
import com.user.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);
    UserResponse getUserById(String id);
    List<UserResponse> getAllUsers();
    UserResponse updateUser(String id, UserRequest userRequest);
    void deleteUser(String id);
}
