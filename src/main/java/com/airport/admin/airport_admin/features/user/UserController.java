package com.airport.admin.airport_admin.features.user;

import com.airport.admin.airport_admin.features.user.dto.CreateUserDto;
import com.airport.admin.airport_admin.features.user.dto.UpdateUserDto;
import com.airport.admin.airport_admin.features.user.dto.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/create")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody CreateUserDto dto) {
        var user = userService.createUser(dto);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserDto dto) {
        dto.setId(id); // Ensure ID from path is set
        var updatedUser = userService.updateUser(dto);
        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }

    @PreAuthorize("hasAnyRole('Admin', 'Supervisor', 'Manager')")
    @GetMapping("/")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        var users = userService.getAllUsers()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasAnyRole('Admin', 'Supervisor', 'Manager')")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
        return userService.findUserByEmail(email)
                .map(userMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('Admin', 'Supervisor', 'Manager')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

}
