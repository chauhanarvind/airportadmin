package com.airport.admin.airport_admin.services;

import com.airport.admin.airport_admin.dto.UserRequestDto;
import com.airport.admin.airport_admin.models.Role;
import com.airport.admin.airport_admin.models.User;
import com.airport.admin.airport_admin.repositories.UserRepository;
import com.airport.admin.airport_admin.repositories.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private UserService(UserRepository userRepository, RoleRepository roleRepository){
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public User createUser(UserRequestDto userRequestDto){
        if(userRepository.findByEmail(userRequestDto.getEmail()).isPresent()){
            throw new RuntimeException("Email already exists:" + userRequestDto.getEmail());
        }

        Role role = roleRepository.findByName(userRequestDto.getRole())
                .orElseThrow(()-> new RuntimeException("Role not found:" + userRequestDto.getRole()));


        User user = new User();
        user.setFirstname(userRequestDto.getFirstName());
        user.setLastname(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(userRequestDto.getPassword());
        user.setRole(role);
    return userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
