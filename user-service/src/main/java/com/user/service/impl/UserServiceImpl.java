package com.user.service.impl;

import com.user.dto.AddressResponse;
import com.user.dto.UserRequest;
import com.user.dto.UserResponse;
import com.user.entity.User;
import com.user.enums.RoleEnum;
import com.user.repository.UserRepository;
import com.user.repository.AddressRepository;
import com.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        log.debug("Generating username for role: {}", userRequest.getRole());
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

        log.info("Saving new user with username: {}", generatedUsername);
        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    @Override
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", id);
                    return new RuntimeException("User not found with id: " + id);
                });
        return mapToUserResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        log.debug("Fetching all users from repository");
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse updateUser(String id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Update failed: User not found with id: {}", id);
                    return new RuntimeException("User not found with id: " + id);
                });

        log.info("Updating user details for id: {}", id);
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
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Delete failed: User not found with id: {}", id);
                    return new RuntimeException("User not found with id: " + id);
                });

        log.info("Soft deleting user with id: {}", id);
        // Soft delete: mark user as inactive
        user.setIsActive(false);
        userRepository.save(user);

        // Soft delete: mark all associated addresses as inactive
        log.debug("Soft deleting addresses for user id: {}", id);
        addressRepository.findByUserId(id).forEach(address -> {
            address.setIsActive(false);
            addressRepository.save(address);
        });
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
                .isActive(user.getIsActive())
                .createdDate(user.getCreatedDate())
                .updatedDate(user.getUpdatedDate())
                .addresses(addressRepository.findByUserId(user.getId()).stream()
                        .filter(address -> address.getIsActive()) // Only include active addresses
                        .map(address -> AddressResponse.builder()
                                .id(address.getId())
                                .street(address.getStreet())
                                .city(address.getCity())
                                .state(address.getState())
                                .postalCode(address.getPostalCode())
                                .country(address.getCountry())
                                .addressType(address.getAddressType())
                                .userId(address.getUserId())
                                .isActive(address.getIsActive())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private String generateUsername(RoleEnum role) {
        String prefix;
        switch (role) {
            case ADMIN:
                prefix = "ADM";
                break;
            case USER:
                prefix = "USR";
                break;
            case MANAGER:
                prefix = "MNG";
                break;
            case RECEPTIONIST:
                prefix = "REC";
                break;
            case WAITER:
                prefix = "WTR";
                break;
            case CLEANER:
                prefix = "CLR";
                break;
            case SECURITY:
                prefix = "SCR";
                break;
            default:
                prefix = "USR";
                break;
        }

        // Count existing users with the same role prefix
        long count = userRepository.findAll().stream()
                .filter(u -> u.getUsername().startsWith(prefix))
                .count();

        return String.format("%s-%06d", prefix, count + 1);
    }
}
