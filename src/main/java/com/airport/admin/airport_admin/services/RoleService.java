package com.airport.admin.airport_admin.services;

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
}
