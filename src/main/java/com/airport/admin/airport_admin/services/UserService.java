package com.airport.admin.airport_admin.services;

import com.airport.admin.airport_admin.dto.UserRequestDto;
import com.airport.admin.airport_admin.models.JobLevel;
import com.airport.admin.airport_admin.models.JobRole;
import com.airport.admin.airport_admin.models.Role;
import com.airport.admin.airport_admin.models.User;
import com.airport.admin.airport_admin.repositories.JobLevelRepository;
import com.airport.admin.airport_admin.repositories.JobRoleRepository;
import com.airport.admin.airport_admin.repositories.UserRepository;
import com.airport.admin.airport_admin.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JobLevelRepository jobLevelRepository;
    private final JobRoleRepository jobRoleRepository;

    private UserService(UserRepository userRepository, RoleRepository roleRepository,
                        JobLevelRepository jobLevelRepository, JobRoleRepository jobRoleRepository){
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.jobLevelRepository = jobLevelRepository;
        this.jobRoleRepository = jobRoleRepository;
    }

    public User createUser(UserRequestDto userRequestDto){
        System.out.println("user request dto =>>>" + userRequestDto);

        if(userRepository.findByEmail(userRequestDto.getEmail()).isPresent()){
            throw new RuntimeException("Email already exists");
        }

        Role role = roleRepository.findById(userRequestDto.getRoleId())
                .orElseThrow(()-> new RuntimeException("User Role not found"));

        JobLevel jobLevel = jobLevelRepository.findById(userRequestDto.getJobLevelId())
                .orElseThrow(()-> new RuntimeException("Job Level Not Found"));

        JobRole jobRole = jobRoleRepository.findById(userRequestDto.getJobRoleId())
                .orElseThrow(()-> new RuntimeException("Job Role Not Found"));

        User user = new User();
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(userRequestDto.getPassword());
        user.setRole(role);
        user.setJobLevel(jobLevel);
        user.setJobRole(jobRole);

    return userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
