package com.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDto {
    private String id;

    @NotBlank(message = "Role name is required")
    private String name;

    private String description;
}
