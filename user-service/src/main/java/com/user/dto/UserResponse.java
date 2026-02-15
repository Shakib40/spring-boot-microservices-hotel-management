package com.user.dto;

import com.user.enum.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private RoleEnum role;
    private List<AddressResponse> addresses;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
