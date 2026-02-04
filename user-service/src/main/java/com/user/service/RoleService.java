package com.user.service;

import com.user.dto.RoleRequest;
import com.user.dto.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse createRole(RoleRequest roleRequest);

    List<RoleResponse> getAllRoles();

    RoleResponse getRoleById(String id);

    RoleResponse updateRole(String id, RoleRequest roleRequest);

    void deleteRole(String id);
}
