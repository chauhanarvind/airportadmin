package com.airport.admin.airport_admin.features.Admin.roles;

import com.airport.admin.airport_admin.utils.DuplicateResourceException;
import com.airport.admin.airport_admin.utils.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role createRole(RoleDto roleDto) {
        if (roleRepository.findByName(roleDto.getName()).isPresent()) {
            throw new DuplicateResourceException("Role with this name already exists");
        }

        Role role = new Role();
        role.setName(roleDto.getName());

        return roleRepository.save(role);
    }

    public Role updateRole(RoleDto roleDto) {
        Role role = roleRepository.findById(roleDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        role.setName(roleDto.getName());
        return roleRepository.save(role);
    }

    public void deleteRole(RoleDto roleDto) {
        Role role = roleRepository.findById(roleDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        roleRepository.delete(role);
    }
}
