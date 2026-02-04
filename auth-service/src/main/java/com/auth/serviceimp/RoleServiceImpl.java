package com.auth.serviceimp;

import com.auth.dto.RoleDto;
import com.auth.entity.Role;
import com.auth.repository.RoleRepository;
import com.auth.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public RoleDto createRole(RoleDto roleDto) {
        if (roleRepository.findByName(roleDto.getName()).isPresent()) {
            throw new RuntimeException("Role already exists with name: " + roleDto.getName());
        }
        Role role = Role.builder()
                .name(roleDto.getName())
                .description(roleDto.getDescription())
                .build();
        Role savedRole = roleRepository.save(role);
        return mapToDto(savedRole);
    }

    @Override
    public RoleDto getRoleById(String id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
        return mapToDto(role);
    }

    @Override
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RoleDto updateRole(String id, RoleDto roleDto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        // updating name if provided and not duplicate
        if (roleDto.getName() != null && !roleDto.getName().isEmpty()) {
            if (!role.getName().equals(roleDto.getName()) && roleRepository.findByName(roleDto.getName()).isPresent()) {
                throw new RuntimeException("Role already exists with name: " + roleDto.getName());
            }
            role.setName(roleDto.getName());
        }

        if (roleDto.getDescription() != null) {
            role.setDescription(roleDto.getDescription());
        }

        Role updatedRole = roleRepository.save(role);
        return mapToDto(updatedRole);
    }

    @Override
    @Transactional
    public void deleteRole(String id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
    }

    private RoleDto mapToDto(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }
}
