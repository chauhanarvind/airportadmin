package com.airport.admin.airport_admin.features.Admin.user;

import com.airport.admin.airport_admin.features.Admin.user.dto.CreateUserDto;
import com.airport.admin.airport_admin.features.Admin.user.dto.UpdateUserDto;
import com.airport.admin.airport_admin.features.Admin.user.dto.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('Admin')") // can be accessed only by admin
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;


    // to create
    @PostMapping("/create")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody CreateUserDto dto) {
        var user = userService.createUser(dto);
        return ResponseEntity.ok(userMapper.toDto(user));
    }


    // to update by id
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserDto dto) {
        dto.setId(id); // Ensure ID from path is set
        var updatedUser = userService.updateUser(dto);
        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }


    //adding pagination and filters
    @GetMapping("/")
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            Pageable pageable
    ) {
        var users = userService.getFilteredUsers(userId, name, email, pageable)
                .map(userMapper::toDto);
        return ResponseEntity.ok(users);
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
