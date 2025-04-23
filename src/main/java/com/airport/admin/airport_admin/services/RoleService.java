package com.airport.admin.airport_admin.services;

import com.airport.admin.airport_admin.dto.RoleDto;
import com.airport.admin.airport_admin.models.JobRole;
import com.airport.admin.airport_admin.models.Role;
import com.airport.admin.airport_admin.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    private RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    public Role createRole(RoleDto roleDto){
        if(roleRepository.findByName(roleDto.getName()).isPresent()){
            throw new RuntimeException("Role with name already exists");
        }

        Role role = new Role();
        role.setName(roleDto.getName());

        return roleRepository.save(role);
    }

    public Role updateRole(RoleDto roleDto){
        Role role = roleRepository.findById(roleDto.getId())
                .orElseThrow(()-> new RuntimeException(roleDto.getName()));

        role.setName(roleDto.getName());
        return  roleRepository.save(role);
    }

    public void deleteRole(RoleDto roleDto){
        Role role = roleRepository.findById(roleDto.getId())
                .orElseThrow(()-> new RuntimeException(roleDto.getName()));

        roleRepository.delete(role);
    }
}
