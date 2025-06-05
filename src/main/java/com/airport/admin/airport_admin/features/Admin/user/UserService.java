package com.airport.admin.airport_admin.features.Admin.user;

import com.airport.admin.airport_admin.utils.AvailabilityConflictException;
import com.airport.admin.airport_admin.utils.ResourceNotFoundException;
import com.airport.admin.airport_admin.features.Admin.constraintProfile.ConstraintProfile;
import com.airport.admin.airport_admin.features.Admin.constraintProfile.ConstraintProfileRepository;
import com.airport.admin.airport_admin.features.Admin.jobLevel.JobLevel;
import com.airport.admin.airport_admin.features.Admin.jobLevel.JobLevelRepository;
import com.airport.admin.airport_admin.features.Admin.jobRole.JobRole;
import com.airport.admin.airport_admin.features.Admin.jobRole.JobRoleRepository;
import com.airport.admin.airport_admin.features.Admin.roles.Role;
import com.airport.admin.airport_admin.features.Admin.roles.RoleRepository;
import com.airport.admin.airport_admin.features.Admin.user.dto.CreateUserDto;
import com.airport.admin.airport_admin.features.Admin.user.dto.UpdateUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            throw new AvailabilityConflictException("Email already exists");
        }

        Role role = resolveRole(dto.getRoleId());
        JobLevel jobLevel = resolveJobLevel(dto.getJobLevelId());
        JobRole jobRole = resolveJobRole(dto.getJobRoleId());
        ConstraintProfile profile = resolveConstraintProfile(dto.getConstraintProfileId());

        User user = userMapper.toEntity(dto, role, jobRole, jobLevel, profile);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        return userRepository.save(user);
    }

    public Page<User> getFilteredUsers(Long userId, String name, String email, Pageable pageable) {
        Specification<User> spec = UserSpecification.build(userId, name, email);
        return userRepository.findAll(spec, pageable);
    }


    public User updateUser(UpdateUserDto dto) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));

        Role role = resolveRole(dto.getRoleId());
        JobLevel jobLevel = resolveJobLevel(dto.getJobLevelId());
        JobRole jobRole = resolveJobRole(dto.getJobRoleId());
        ConstraintProfile profile = resolveConstraintProfile(dto.getConstraintProfileId());

        userMapper.updateEntity(user, dto, role, jobRole, jobLevel, profile);

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User does not exist");
        }
        userRepository.deleteById(id);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    // --- Helper Methods ---

    private Role resolveRole(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("User role not found"));
    }

    private JobRole resolveJobRole(Long jobRoleId) {
        return jobRoleRepository.findById(jobRoleId)
                .orElseThrow(() -> new ResourceNotFoundException("Job role does not exist"));
    }

    private JobLevel resolveJobLevel(Long jobLevelId) {
        return jobLevelRepository.findById(jobLevelId)
                .orElseThrow(() -> new ResourceNotFoundException("Job level not found"));
    }

    private ConstraintProfile resolveConstraintProfile(Long profileId) {
        if (profileId == null) return null;
        return constraintProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Constraint profile not found"));
    }
}
