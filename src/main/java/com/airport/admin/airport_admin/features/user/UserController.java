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


    @PostMapping("/create")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody CreateUserDto dto) {
        var user = userService.createUser(dto);
        return ResponseEntity.ok(userMapper.toDto(user));
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserDto dto) {
        dto.setId(id); // Ensure ID from path is set
        var updatedUser = userService.updateUser(dto);
        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }


    @GetMapping("/")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        var users = userService.getAllUsers()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
        return userService.findUserByEmail(email)
                .map(userMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

}
