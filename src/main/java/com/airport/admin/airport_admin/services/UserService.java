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

        Role role = resolveRole(userRequestDto.getRoleId());
        JobLevel jobLevel = resolveJobLevel(userRequestDto.getJobLevelId());
        JobRole jobRole = resolveJobRole(userRequestDto.getJobRoleId());

        String rawPassword = userRequestDto.getPassword();
        if(rawPassword == null || rawPassword.isBlank()){
            throw new RuntimeException("Password is required for new users");
        }
        if(rawPassword.length() < 6){
            throw  new RuntimeException("Password must be at least 6 characters");
        }
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

    public User updateUser(UserRequestDto userRequestDto){


        User user = userRepository.findById(userRequestDto.getId())
                .orElseThrow(() -> new RuntimeException("User does not exist"));


        Role role = resolveRole(userRequestDto.getRoleId());
        JobRole jobRole= resolveJobRole(userRequestDto.getJobRoleId());
        JobLevel jobLevel= resolveJobLevel(userRequestDto.getJobLevelId());

        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
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

    private Role resolveRole(Long roleId){
        return roleRepository.findById(roleId)
                .orElseThrow(()-> new RuntimeException("User role not found"));
    }

    private JobRole resolveJobRole(Long jobRoleId){
        return jobRoleRepository.findById(jobRoleId)
                .orElseThrow(()-> new RuntimeException("Job role does not exists"));
    }

    private JobLevel resolveJobLevel(Long jobLevelId){
        return jobLevelRepository.findById(jobLevelId)
                .orElseThrow(()-> new RuntimeException("Job Level Not Found"));
    }
}
