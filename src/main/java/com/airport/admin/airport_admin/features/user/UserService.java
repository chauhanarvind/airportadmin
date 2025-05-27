package com.airport.admin.airport_admin.features.user;

import com.airport.admin.airport_admin.features.constraintProfile.ConstraintProfile;
import com.airport.admin.airport_admin.features.constraintProfile.ConstraintProfileRepository;
import com.airport.admin.airport_admin.features.jobLevel.JobLevel;
import com.airport.admin.airport_admin.features.jobLevel.JobLevelRepository;
import com.airport.admin.airport_admin.features.jobRole.JobRole;
import com.airport.admin.airport_admin.features.jobRole.JobRoleRepository;
import com.airport.admin.airport_admin.features.roles.Role;
import com.airport.admin.airport_admin.features.roles.RoleRepository;
import com.airport.admin.airport_admin.features.user.dto.CreateUserDto;
import com.airport.admin.airport_admin.features.user.dto.UpdateUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JobLevelRepository jobLevelRepository;
    private final JobRoleRepository jobRoleRepository;
    private final ConstraintProfileRepository constraintProfileRepository;
    private final UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(CreateUserDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Role role = resolveRole(dto.getRoleId());
        JobLevel jobLevel = resolveJobLevel(dto.getJobLevelId());
        JobRole jobRole = resolveJobRole(dto.getJobRoleId());
        ConstraintProfile profile = resolveConstraintProfile(dto.getConstraintProfileId());

        User user = userMapper.toEntity(dto, role, jobRole, jobLevel, profile);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        return userRepository.save(user);
    }

    public User updateUser(UpdateUserDto dto) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        Role role = resolveRole(dto.getRoleId());
        JobLevel jobLevel = resolveJobLevel(dto.getJobLevelId());
        JobRole jobRole = resolveJobRole(dto.getJobRoleId());
        ConstraintProfile profile = resolveConstraintProfile(dto.getConstraintProfileId());

        // No password update here since it's not part of the DTO
        userMapper.updateEntity(user, dto, role, jobRole, jobLevel, profile);

        return userRepository.save(user);
    }


    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User does not exist");
        }
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // --- Helper Methods ---

    private Role resolveRole(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("User role not found"));
    }

    private JobRole resolveJobRole(Long jobRoleId) {
        return jobRoleRepository.findById(jobRoleId)
                .orElseThrow(() -> new RuntimeException("Job role does not exist"));
    }

    private JobLevel resolveJobLevel(Long jobLevelId) {
        return jobLevelRepository.findById(jobLevelId)
                .orElseThrow(() -> new RuntimeException("Job level not found"));
    }

    private ConstraintProfile resolveConstraintProfile(Long profileId) {
        if (profileId == null) return null;
        return constraintProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Constraint profile not found"));
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }


}
