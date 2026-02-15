package com.user.service.impl;

import com.user.dto.AddressResponse;
import com.user.dto.UserRequest;
import com.user.dto.UserResponse;
import com.user.entity.User;
import com.user.enum.RoleEnum;
import com.user.repository.UserRepository;
import com.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // here userRequest.getUsername() will auto update like USER-000001
    // if role is  ADMIN then ADM-000001
    // if role id USER then USR-000001
    // if role id MANAGER then MNG-000001
    // if role id RECEPTIONIST then REC-000001
    // if role id WAITER then WTR-000001
    // if role id CLEANER then CLR-000001
    // if role id SECURITY then SCR-000001
    

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        String generatedUsername = generateUsername(userRequest.getRole());
        
        User user = User.builder()
                .username(generatedUsername)
                .firstname(userRequest.getFirstname())
                .lastname(userRequest.getLastname())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .phoneNumber(userRequest.getPhoneNumber())
                .role(userRequest.getRole())
                .build();

        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    @Override
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return mapToUserResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse updateUser(String id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setUsername(userRequest.getUsername());
        user.setFirstname(userRequest.getFirstname());
        user.setLastname(userRequest.getLastname());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setRole(userRequest.getRole());

        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    @Override
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .createdDate(user.getCreatedDate())
                .updatedDate(user.getUpdatedDate())
                .addresses(user.getAddresses() != null ? user.getAddresses().stream()
                        .map(address -> AddressResponse.builder()
                                .id(address.getId())
                                .street(address.getStreet())
                                .city(address.getCity())
                                .state(address.getState())
                                .postalCode(address.getPostalCode())
                                .country(address.getCountry())
                                .addressType(address.getAddressType())
                                .userId(user.getId())
                                .build())
                        .collect(Collectors.toList()) : null)
                .build();
    }
}
