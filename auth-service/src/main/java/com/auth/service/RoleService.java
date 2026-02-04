package com.auth.service;

import com.auth.dto.RoleDto;
import java.util.List;

public interface RoleService {
    RoleDto createRole(RoleDto roleDto);

    RoleDto getRoleById(String id);

    List<RoleDto> getAllRoles();

    RoleDto updateRole(String id, RoleDto roleDto);

    void deleteRole(String id);
}
